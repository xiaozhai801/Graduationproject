package com.zpq.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpq.pojo.Articleinfo;
import com.zpq.pojo.Useractions;
import com.zpq.pojo.Userinfo;
import com.zpq.utils.DBUtil;

public class UseractionsDaoImpl implements UseractionsDao {

	@Override
	public Map<String, Useractions> searchUseractions(String element1, String value1,String element2, String value2) throws SQLException {
		// TODO Auto-generated method stub
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		Useractions user = new Useractions();
		Map<String, Useractions> useractions=new HashMap<>();
		String sql = "SELECT * FROM c_useractions where " + element1 + "=? and "+ element2 + "=?" ;
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setObject(1, value1);
		ps.setObject(2, value2);

		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			user.setActionId(rs.getLong("actionId"));
			user.setName(rs.getString("name"));
			user.setArticleId(rs.getLong("articleId"));
			user.setTopic(rs.getString("topic"));
			user.setLike(rs.getBoolean("like"));
			user.setFavorite(rs.getBoolean("favorite"));
			useractions.put(user.getName(), user);
		}
		return useractions;
	}

	@Override
	public List<Object> searchUseractions(Long articleId, String userName) throws SQLException {
	    UserinfoDao userinfoDao = new UserinfoDaoImpl();
	    List<Userinfo> userinfoList = userinfoDao.selectPerSonalInfo(userName);
	    // 非空判断避免空指针
	    if (userinfoList == null || userinfoList.isEmpty()) {
	        return new ArrayList<>();
	    }
	    Userinfo userinfo = userinfoList.get(0);
	    String name = userinfo.getName();
	    
	    // 获取文章的topic
	    String topic = "";
	    ArticleinfoDao articleinfoDao=new ArticleinfoDaoImpl();
	    List<Articleinfo> articleInfos = articleinfoDao.searchArticleInfo(articleId);
	    if (articleInfos != null && !articleInfos.isEmpty()) {
	        topic = articleInfos.get(0).getTopic();
	    }
	    
	    DBUtil dbUtil = new DBUtil();
	    List<Object> useractionsList = new ArrayList<>();
	    
	    // 1. 存在性判断
	    String checkSql = "SELECT COUNT(1) FROM c_useractions WHERE articleId=? AND name=?";
	    PreparedStatement checkPs = dbUtil.getPreparedStatement(checkSql);
	    checkPs.setLong(1, articleId);
	    checkPs.setString(2, name);
	    ResultSet checkRs = checkPs.executeQuery();
	    
	    boolean dataExists = false;
	    if (checkRs.next()) {
	        dataExists = checkRs.getInt(1) > 0;
	    }
	    
	    // 2. 不存在则插入
	    if (!dataExists) {
	        String insertSql = "INSERT INTO c_useractions (name, articleId, topic, `like`, favorite) VALUES (?, ?, ?, 0, 0)";
	        PreparedStatement insertPs = dbUtil.getPreparedStatement(insertSql);
	        insertPs.setString(1, name);
	        insertPs.setLong(2, articleId);
	        insertPs.setString(3, topic);
	        insertPs.executeUpdate();
	    }
	    
	    // 3. 查询最新数据
	    String sql = "SELECT * FROM c_useractions where articleId=? and name=?";
	    PreparedStatement ps = dbUtil.getPreparedStatement(sql);
	    ps.setLong(1, articleId);
	    ps.setString(2, name);
	    ResultSet rs = ps.executeQuery();

	    while (rs.next()) {
	        Useractions useractions = new Useractions();
	        useractions.setActionId(rs.getLong("actionId"));
	        useractions.setName(rs.getString("name"));
	        useractions.setArticleId(rs.getLong("articleId"));
	        useractions.setTopic(rs.getString("topic"));
	        useractions.setLike(rs.getInt("like") == 1);
	        useractions.setFavorite(rs.getInt("favorite") == 1);
	        useractionsList.add(useractions);
	    }
	    
	    return useractionsList;
	}

	@Override
	public int updateUserActions(String userName, long articleId, String like, String favorite) throws SQLException {
	    DBUtil dbUtil = new DBUtil();
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    int rowsAffected = 0;
	    boolean recordExists = false;

	    try {
	        // 1. 查询文章信息获取topic
	        String topic = null;
	        ArticleinfoDao articleinfoDao = new ArticleinfoDaoImpl();
	        Map<Long, Articleinfo> articleMap = articleinfoDao.searchArticleInfo("articleId", articleId);
	        if (articleMap != null && !articleMap.isEmpty()) {
	            Articleinfo article = articleMap.get(articleId);
	            if (article != null) {
	                topic = article.getTopic();
	            }
	        }
	        if (topic == null) return -1; // 文章不存在

	        // 2. 查询用户名对应的name
	        String name = null;
	        UserinfoDao userinfoDao=new UserinfoDaoImpl();
	        Map<String, Userinfo> userMap = userinfoDao.searchUserInfo("userName", userName);
	        if (userMap != null && !userMap.isEmpty()) {
	            Userinfo user = userMap.get(userName);
	            if (user != null) name = user.getName();
	        }
	        if (name == null) return -2; // 用户不存在

	        // 3. 检查用户操作记录是否存在（唯一键：name + articleId）
	        String checkSql = "SELECT actionId, `like`, favorite FROM c_useractions WHERE name = ? AND articleId = ?";
	        ps = dbUtil.getPreparedStatement(checkSql);
	        ps.setString(1, name);
	        ps.setLong(2, articleId);
	        rs = ps.executeQuery();

	        // 记录原状态
	        boolean oldLike = false;
	        boolean oldFavorite = false;
	        long actionId = -1;

	        if (rs.next()) {
	            recordExists = true;
	            actionId = rs.getLong("actionId");
	            oldLike = rs.getBoolean("like");
	            oldFavorite = rs.getBoolean("favorite");
	        }
	        closeResources(rs, ps);

	        // 转换参数为Boolean
	        Boolean likeFlag = null;
	        if ("true".equals(like)) {
	            likeFlag = true;
	        } else if ("false".equals(like)) {
	            likeFlag = false;
	        }

	        Boolean favoriteFlag = null;
	        if ("true".equals(favorite)) {
	            favoriteFlag = true;
	        } else if ("false".equals(favorite)) {
	            favoriteFlag = false;
	        }

	        // 4. 处理用户操作记录（更新或插入）
	        if (recordExists) {
	            // 记录存在：更新非null字段
	            StringBuilder updateSql = new StringBuilder("UPDATE c_useractions SET topic = ?");
	            List<Object> params = new ArrayList<>();
	            params.add(topic);

	            if (likeFlag != null) {
	                updateSql.append(", `like` = ?");
	                params.add(likeFlag);
	            }
	            if (favoriteFlag != null) {
	                updateSql.append(", favorite = ?");
	                params.add(favoriteFlag);
	            }

	            updateSql.append(" WHERE actionId = ?");
	            params.add(actionId);

	            ps = dbUtil.getPreparedStatement(updateSql.toString());
	            for (int i = 0; i < params.size(); i++) {
	                ps.setObject(i + 1, params.get(i));
	            }
	            rowsAffected = ps.executeUpdate();
	        } else {
	            // 记录不存在：插入新记录（会增加views计数）
	            String insertSql = "INSERT INTO c_useractions (name, articleId, `like`, favorite, topic) VALUES (?, ?, ?, ?, ?)";
	            ps = dbUtil.getPreparedStatement(insertSql);
	            ps.setString(1, name);
	            ps.setLong(2, articleId);
	            ps.setBoolean(3, likeFlag != null ? likeFlag : false);
	            ps.setBoolean(4, favoriteFlag != null ? favoriteFlag : false);
	            ps.setString(5, topic);
	            rowsAffected = ps.executeUpdate();
	        }
	        closeResources(null, ps);

	        // 5. 统计并更新文章表的点赞数（从c_useractions统计）
	        if (likeFlag != null && likeFlag != oldLike) {
	            String countLikeSql = "SELECT COUNT(*) AS likeTotal FROM c_useractions WHERE articleId = ? AND `like` = true";
	            ps = dbUtil.getPreparedStatement(countLikeSql);
	            ps.setLong(1, articleId);
	            rs = ps.executeQuery();
	            int likeTotal = 0;
	            if (rs.next()) {
	                likeTotal = rs.getInt("likeTotal");
	            }
	            closeResources(rs, ps);

	            String updateLikeSql = "UPDATE v_articleinfo SET likes = ? WHERE articleId = ?";
	            ps = dbUtil.getPreparedStatement(updateLikeSql);
	            ps.setInt(1, likeTotal);
	            ps.setLong(2, articleId);
	            ps.executeUpdate();
	            closeResources(null, ps);
	        }

	        // 6. 统计并更新文章表的收藏数（从c_useractions统计）
	        if (favoriteFlag != null && favoriteFlag != oldFavorite) {
	            String countFavoriteSql = "SELECT COUNT(*) AS favoriteTotal FROM c_useractions WHERE articleId = ? AND favorite = true";
	            ps = dbUtil.getPreparedStatement(countFavoriteSql);
	            ps.setLong(1, articleId);
	            rs = ps.executeQuery();
	            int favoriteTotal = 0;
	            if (rs.next()) {
	                favoriteTotal = rs.getInt("favoriteTotal");
	            }
	            closeResources(rs, ps);

	            String updateFavoriteSql = "UPDATE v_articleinfo SET favorites = ? WHERE articleId = ?";
	            ps = dbUtil.getPreparedStatement(updateFavoriteSql);
	            ps.setInt(1, favoriteTotal);
	            ps.setLong(2, articleId);
	            ps.executeUpdate();
	            closeResources(null, ps);
	        }

	        // 7. 新增：统计并更新文章表的浏览量（views = c_useractions中articleId的总记录数）
	        // 每次操作（新增或更新）都重新统计，确保浏览量准确
	        String countViewsSql = "SELECT COUNT(*) AS viewsTotal FROM c_useractions WHERE articleId = ?";
	        ps = dbUtil.getPreparedStatement(countViewsSql);
	        ps.setLong(1, articleId);
	        rs = ps.executeQuery();
	        int viewsTotal = 0;
	        if (rs.next()) {
	            viewsTotal = rs.getInt("viewsTotal");
	        }
	        closeResources(rs, ps);

	        String updateViewsSql = "UPDATE v_articleinfo SET views = ? WHERE articleId = ?";
	        ps = dbUtil.getPreparedStatement(updateViewsSql);
	        ps.setInt(1, viewsTotal);
	        ps.setLong(2, articleId);
	        ps.executeUpdate();
	        closeResources(null, ps);

	    } finally {
	        closeResources(rs, ps);
	    }

	    return rowsAffected > 0 ? 1 : 0;
	}

	// 关闭数据库资源
	private void closeResources(ResultSet rs, PreparedStatement ps) {
	    try {
	        if (rs != null) rs.close();
	        if (ps != null) ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public List<Object> SelectMyFavorite(String userName) throws SQLException {
		// TODO Auto-generated method stub
		UserinfoDao userinfoDao=new UserinfoDaoImpl();
	    Map<String, Userinfo> userMap = userinfoDao.searchUserInfo("userName", userName);
	    if (userMap == null || userMap.isEmpty()) {
	        return new ArrayList<>(); 
	    }
	    String name = userMap.get(userName).getName();
	    if (name == null) {
	        return new ArrayList<>();
	    }
		
		List<Object> myFavoriteList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT * FROM v_articleinfo where articleId in (SELECT articleId FROM c_useractions where name=? and favorite=1)";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, name);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Articleinfo articleinfo = new Articleinfo();
			articleinfo.setArticleId(rs.getLong("articleId"));
			articleinfo.setUserId(rs.getInt("userId"));
			articleinfo.setName(rs.getString("name"));
			articleinfo.setTopic(rs.getString("topic"));
			articleinfo.setTypeName(rs.getString("typeName"));
			articleinfo.setUploadTime(rs.getString("uploadTime"));
			articleinfo.setRelease(rs.getInt("release"));
			articleinfo.setViews(rs.getInt("views"));
			articleinfo.setLikes(rs.getInt("likes"));
			articleinfo.setFavorites(rs.getInt("favorites"));
			articleinfo.setDataHtml(rs.getString("dataHtml"));
			myFavoriteList.add(articleinfo);
		}
		return myFavoriteList;
	}

	@Override
	public int CountMyFavorite(String userName) throws SQLException {
		// TODO Auto-generated method stub
		UserinfoDao userinfoDao=new UserinfoDaoImpl();
	    Map<String, Userinfo> userMap = userinfoDao.searchUserInfo("userName", userName);
	    if (userMap == null || userMap.isEmpty()) {
	        return 0;
	    }
	    String name = userMap.get(userName).getName();
	    if (name == null) {
	        return 0;
	    }
	    
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT count(*) as sum FROM c_useractions where name=? and favorite=1";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, name);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getInt("sum");
		}
		return 0;
	}

	@Override
	public List<Object> SelectMyLike(String userName) throws SQLException {
		UserinfoDao userinfoDao=new UserinfoDaoImpl();
	    Map<String, Userinfo> userMap = userinfoDao.searchUserInfo("userName", userName);
	    if (userMap == null || userMap.isEmpty()) {
	        return new ArrayList<>(); 
	    }
	    String name = userMap.get(userName).getName();
	    if (name == null) {
	        return new ArrayList<>();
	    }
		
		List<Object> myFavoriteList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT * FROM v_articleinfo where articleId in (SELECT articleId FROM c_useractions where name=? and `like`=1)";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, name);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Articleinfo articleinfo = new Articleinfo();
			articleinfo.setArticleId(rs.getLong("articleId"));
			articleinfo.setUserId(rs.getInt("userId"));
			articleinfo.setName(rs.getString("name"));
			articleinfo.setTopic(rs.getString("topic"));
			articleinfo.setTypeName(rs.getString("typeName"));
			articleinfo.setUploadTime(rs.getString("uploadTime"));
			articleinfo.setRelease(rs.getInt("release"));
			articleinfo.setViews(rs.getInt("views"));
			articleinfo.setLikes(rs.getInt("likes"));
			articleinfo.setFavorites(rs.getInt("favorites"));
			articleinfo.setDataHtml(rs.getString("dataHtml"));
			myFavoriteList.add(articleinfo);
		}
		return myFavoriteList;
	}

	@Override
	public int CountMyLike(String userName) throws SQLException {
		// TODO Auto-generated method stub
		UserinfoDao userinfoDao=new UserinfoDaoImpl();
	    Map<String, Userinfo> userMap = userinfoDao.searchUserInfo("userName", userName);
	    if (userMap == null || userMap.isEmpty()) {
	        return 0; 
	    }
	    String name = userMap.get(userName).getName();
	    if (name == null) {
	        return 0;
	    }
		
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT count(*) as sum FROM c_useractions where name=? and `like`=1";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, name);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getInt("sum");
		}
		return 0;
	}

}
