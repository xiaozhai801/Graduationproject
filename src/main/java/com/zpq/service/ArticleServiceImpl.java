package com.zpq.service;

import com.zpq.dao.ArticleinfoDao;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.zpq.dao.ArticleinfoDaoImpl;
import com.zpq.dao.ModeltypeDao;
import com.zpq.dao.ModeltypeDaoImpl;
import com.zpq.dao.UseractionsDao;
import com.zpq.dao.UseractionsDaoImpl;
import com.zpq.dao.UsercommentDao;
import com.zpq.dao.UsercommentDaoImpl;
import com.zpq.dao.UserinfoDao;
import com.zpq.dao.UserinfoDaoImpl;
import com.zpq.pojo.Articleinfo;
import com.zpq.pojo.Modeltype;
import com.zpq.pojo.Userinfo;
import com.zpq.pojo.Vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArticleServiceImpl implements ArticleService {
    private ArticleinfoDao articleDao = new ArticleinfoDaoImpl();

    @Override
    public Vo getAllAuditArticles(int page, int limit) throws SQLException {
        Vo vo = new Vo();
        List<Object> articleList = articleDao.selectAuditArticle(page, limit);
        int totalCount = articleDao.countAuditArticle();

        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(totalCount);
        vo.setData(articleList);
        return vo;
    }
    
    @Override
    public boolean deleteUserArticle(int articleId) throws SQLException {
        return articleDao.deleteArticle(articleId) == 1;
    }

    @Override
    public boolean deleteAdminArticle(int articleId) throws SQLException {
        return articleDao.deleteArticle(articleId) == 1;
    }
    
    @Override
    public Vo saveDraftArticle(String userName, String topic, String dataHtml, int typeId) throws SQLException {
        Vo vo = new Vo();
        vo.setCount(0);
        vo.setData(null);

        // 1. 参数校验（标题、类型ID不能为空）
        if (topic == null || topic.trim().isEmpty()) {
            vo.setCode(1);
            vo.setMsg("标题不能为空");
            return vo;
        }
        if (typeId <= 0) {
            vo.setCode(2);
            vo.setMsg("请选择文章类型");
            return vo;
        }

        UserinfoDao userDao=new UserinfoDaoImpl();
		ModeltypeDao modelDao=new ModeltypeDaoImpl();
        
        // 2. 获取用户信息（通过用户名查询用户ID和姓名）
        Map<String, Userinfo> userInfoMap = userDao.searchUserInfo("userName", userName);
        if (userInfoMap == null || !userInfoMap.containsKey(userName)) {
            vo.setCode(3);
            vo.setMsg("用户不存在");
            return vo;
        }
        Userinfo user = userInfoMap.get(userName);

        // 3. 获取文章类型名称（通过typeId查询）
        Map<Integer, Modeltype> modelInfoMap = modelDao.searchModelInfo("typeId", typeId);
        if (modelInfoMap == null || !modelInfoMap.containsKey(typeId)) {
            vo.setCode(4);
            vo.setMsg("文章类型不存在");
            return vo;
        }
        Modeltype model = modelInfoMap.get(typeId);

        // 4. 生成文章ID（当前时间戳格式化）
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddss");
        int articleId;
        try {
            articleId = Integer.parseInt(now.format(formatter));
        } catch (NumberFormatException e) {
            vo.setCode(5);
            vo.setMsg("生成文章ID失败");
            return vo;
        }

        // 5. 封装文章信息（草稿状态：release=0）
        Articleinfo article = new Articleinfo();
        article.setArticleId(articleId);
        article.setUserId(user.getUserId());
        article.setName(user.getName());
        article.setTopic(topic);
        article.setTypeName(model.getTypeName());
        article.setRelease(0); // 0表示草稿
        // 处理HTML路径（替换相对路径为绝对路径）
        article.setDataHtml(dataHtml.replace("../../", "/RepairSystem/"));

        // 6. 调用Dao层保存草稿
        int result = articleDao.uploadArticleInfo(article);
        if (result == 1) {
            vo.setCode(0);
            vo.setMsg("success");
        } else {
            vo.setCode(6);
            vo.setMsg("fail");
        }

        return vo;
    }
    
    @Override
    public Vo publishArticleForReview(String userName, long articleId, int status, String topic, String dataHtml, int typeId) throws SQLException {
        Vo vo = new Vo();
        vo.setCount(0);
        vo.setData(null);

        // 1. 基础校验：用户、状态、文章ID
        if (userName == null || userName.trim().isEmpty()) {
            vo.setCode(1);
            vo.setMsg("请先登录");
            return vo;
        }
        if (status != 1 && articleId <= 0) { // 非直接发布时，必须传入有效草稿ID
            vo.setCode(2);
            vo.setMsg("草稿ID无效");
            return vo;
        }

        // 2. 封装文章信息（区分“直接发布”和“草稿转发布”）
        Articleinfo article = new Articleinfo();
        article.setArticleId((int) articleId); // 转换为int（与原逻辑一致）
        article.setRelease(1); // 1表示待审核（发布状态）

        // 3. 直接发布（status=1）：需补充标题、HTML内容、类型等完整信息
        if (status == 1) {
            // 校验直接发布的必填参数
            if (topic == null || topic.trim().isEmpty()) {
                vo.setCode(3);
                vo.setMsg("文章标题不能为空");
                return vo;
            }
            if (typeId <= 0) {
                vo.setCode(4);
                vo.setMsg("请选择文章类型");
                return vo;
            }
            
            UserinfoDao userDao=new UserinfoDaoImpl();
    		ModeltypeDao modelDao=new ModeltypeDaoImpl();

            // 获取用户信息（用户ID、姓名）
            Map<String, Userinfo> userInfoMap = userDao.searchUserInfo("userName", userName);
            if (userInfoMap == null || !userInfoMap.containsKey(userName)) {
                vo.setCode(5);
                vo.setMsg("用户不存在");
                return vo;
            }
            Userinfo user = userInfoMap.get(userName);
            article.setUserId(user.getUserId());
            article.setName(user.getName());
            article.setTopic(topic);

            // 获取文章类型名称
            Map<Integer, Modeltype> modelInfoMap = modelDao.searchModelInfo("typeId", typeId);
            if (modelInfoMap == null || !modelInfoMap.containsKey(typeId)) {
                vo.setCode(6);
                vo.setMsg("文章类型不存在");
                return vo;
            }
            article.setTypeName(modelInfoMap.get(typeId).getTypeName());

            // 处理HTML路径（替换相对路径为绝对路径）
            article.setDataHtml(dataHtml.replace("../../", "/RepairSystem/"));
        }

        // 4. 调用Dao层执行发布（新增或更新文章状态）
        int result = articleDao.uploadArticleInfo(article);
        if (result == 1) {
            vo.setCode(0);
            vo.setMsg("success");
        } else {
            vo.setCode(7);
            vo.setMsg("fail");
        }

        return vo;
    }

    public String generateArticleId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddss");
        return now.format(formatter);
    }
    
    @Override
    public Vo searchArticles(Articleinfo article, int page, int limit) throws SQLException {
        Vo vo = new Vo();
        // 1. 调用Dao层执行搜索（多条件+分页）
        List<Object> articleList = articleDao.searchArticleInfo(article, page, limit);
        // 2. 调用Dao层统计符合条件的总条数
        int totalCount = articleDao.countArticle(article);
        // 3. 封装Vo响应对象
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(totalCount);
        vo.setData(articleList);
        return vo;
    }
    
    @Override
    public Vo searchMyArticles(Articleinfo articleinfo, int page, int limit) throws SQLException {
        Vo vo = new Vo();
        List<Object> articleList = articleDao.searchMyArticle(articleinfo, page, limit);
        int totalCount = articleDao.countMyArticle(articleinfo);
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(totalCount);
        vo.setData(articleList);
        return vo;
    }
    
    @Override
    public Vo searchMyDraftArticles(Articleinfo articleinfo, int page, int limit) throws SQLException {
        Vo vo = new Vo();
        List<Object> articleList = articleDao.searchMyDraftArticle(articleinfo, page, limit);
        int totalCount = articleDao.countMyDraftArticle(articleinfo);
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(totalCount);
        vo.setData(articleList);
        return vo;
    }
    
    @Override
    public Vo showArticleContent(long articleId) throws SQLException {
        Vo vo = new Vo();
        List<Object> resultList = new ArrayList<>();

        // 1. 调用Dao层查询文章信息
        Map<Long, Articleinfo> articleInfoMap = articleDao.searchArticleInfo("articleId", articleId);
        if (articleInfoMap == null || !articleInfoMap.containsKey(articleId)) {
            vo.setCode(1);
            vo.setMsg("文章不存在");
            vo.setCount(0);
            vo.setData(resultList);
            return vo;
        }
        Articleinfo article = articleInfoMap.get(articleId);

        resultList.add(article);

        // 3. 封装Vo响应（count用总文章数，与原逻辑一致）
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(articleDao.countArticle()); // 统计所有文章总数
        vo.setData(resultList);
        return vo;
    }
    
    @Override
    public Vo reviewArticle(long articleId, int passCode, int operatorRole) throws SQLException {
        Vo vo = new Vo();
        vo.setCount(0);
        vo.setData(new ArrayList<>());

        // 1. 权限校验：仅管理员（role=0）可执行审核操作
        if (operatorRole != 0) {
            vo.setCode(1);
            vo.setMsg("无审核权限，仅管理员可操作");
            return vo;
        }

        // 2. 审核状态校验：仅支持passCode=1（通过）或2（不通过）
        if (passCode != 1 && passCode != 2) {
            vo.setCode(2);
            vo.setMsg("审核状态无效");
            return vo;
        }

        // 3. 动态映射状态：passCode=1→3（通过），passCode=2→2（不通过）
        int releaseStatus = (passCode == 1) ? 3 : 2;
        // 调用合并后的Dao方法
        int result = articleDao.updateArticleReleaseStatus(articleId, releaseStatus);

        // 4. 封装审核结果
        if (result == 1) {
            vo.setCode(0);
            vo.setMsg("success");
        } else {
            vo.setCode(3);
            vo.setMsg("审核失败：文章不存在或状态异常");
        }

        return vo;
    }

    @Override
    public Vo showArticleDetail(long articleId, String userName) throws SQLException {
        Vo vo = new Vo();
        // 1. 用于封装文章详情、用户互动、用户评论三类数据（原Map逻辑保留）
        Map<String, Object> detailData = new HashMap<>();

        // 2. 查询文章基础信息（复用现有逻辑）
        Map<Long, Articleinfo> articleMap = articleDao.searchArticleInfo("articleId", articleId);
        if (articleMap == null || !articleMap.containsKey(articleId)) {
            vo.setCode(1);
            vo.setMsg("文章不存在");
            vo.setCount(0);
            vo.setData(new ArrayList<>()); // 空数据时也传空List，避免Null
            return vo;
        }
        Articleinfo article = articleMap.get(articleId);
        detailData.put("articleInfo", article);

        // 3. 实例化Dao并查询互动、评论数据（修正泛型为具体类型，更规范）
        UseractionsDao actionsDao = new UseractionsDaoImpl();
        UsercommentDao commentDao = new UsercommentDaoImpl();
        List<Object> userActions = actionsDao.searchUseractions(articleId, userName);
        List<Object> userComments = commentDao.searchUsercomment(articleId);
        detailData.put("userActions", userActions);
        detailData.put("userComments", userComments);

        // 4. 将Map包装成List，匹配Vo的setData方法参数类型
        List<Object> dataList = new ArrayList<>();
        dataList.add(detailData); // 把所有数据的Map作为一个元素存入List

        // 5. 封装Vo响应
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(userComments.size());
        vo.setData(dataList);

        return vo;
    }
    
    @Override
    public Vo selectMyArticles(Articleinfo articleinfo, int page, int limit) throws SQLException {
        Vo vo = new Vo();
        vo.setData(new ArrayList<>());

        // 1. 业务参数校验
        if (page < 1 || limit < 1) {
            vo.setCode(3);
            vo.setMsg("分页参数无效（页码和条数必须大于0）");
            vo.setCount(0);
            return vo;
        }
        if (articleinfo.getName() == null || articleinfo.getName().isEmpty()) {
            vo.setCode(6);
            vo.setMsg("作者名称为空，无法查询");
            vo.setCount(0);
            return vo;
        }

        // 2. 调用DAO层查询（传入name）
        List<Object> articleList = articleDao.selectMyArticle(articleinfo.getName(), page, limit);
        int totalCount = articleDao.countMyArticle(articleinfo);

        // 3. 封装结果
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(totalCount);
        vo.setData(articleList);
        return vo;
    }

	@Override
	public Vo selectMyDraftArticles(Articleinfo articleinfo, int page, int limit) throws SQLException {
		// TODO Auto-generated method stub
        Vo vo = new Vo();
        vo.setData(new ArrayList<>());

        // 1. 业务参数校验
        if (page < 1 || limit < 1) {
            vo.setCode(3);
            vo.setMsg("分页参数无效（页码和条数必须大于0）");
            vo.setCount(0);
            return vo;
        }
        if (articleinfo.getName() == null || articleinfo.getName().isEmpty()) {
            vo.setCode(6);
            vo.setMsg("作者名称为空，无法查询");
            vo.setCount(0);
            return vo;
        }

        // 2. 调用DAO层查询（传入name）
        List<Object> articleList = articleDao.selectMyDraftArticle(articleinfo.getName(), page, limit);
        int totalCount = articleDao.countMyDraftArticle(articleinfo);

        // 3. 封装结果
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(totalCount);
        vo.setData(articleList);
        return vo;
	}
	
	 @Override
	    public Vo countModelArticles() throws SQLException {
	        Vo vo = new Vo();
	        List<Object> modelCountList = new ArrayList<>();
	        Map<String, Integer> articleCountMap = new HashMap<>();

	        int phoneCount = articleDao.countModelArticle("手机");
	        int laptopCount = articleDao.countModelArticle("笔记本电脑");
	        int consoleCount = articleDao.countModelArticle("游戏主机");
	        int allCount = articleDao.countModelArticle("all");

	        articleCountMap.put("phone", phoneCount);
	        articleCountMap.put("laptop", laptopCount);
	        articleCountMap.put("console", consoleCount);
	        articleCountMap.put("all", allCount);
	        modelCountList.add(articleCountMap);

	        vo.setCode(0);
	        vo.setMsg("success");
	        vo.setCount(allCount);
	        vo.setData(modelCountList);

	        return vo;
	    }
}