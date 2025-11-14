package com.zpq.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpq.pojo.Articleinfo;
import com.zpq.pojo.Usercomment;
import com.zpq.pojo.Userinfo;
import com.zpq.utils.DBUtil;

public class UsercommentDaoImpl implements UsercommentDao {

	@Override
	public Map<String, Usercomment> searchUsercomment(String element1, String value1,String element2, String value2) throws SQLException {
		// TODO Auto-generated method stub
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		Usercomment user = new Usercomment();
		Map<String, Usercomment> usercomment=new HashMap<>();
		String sql = "SELECT * FROM c_usercomment where " + element1 + "=? and "+ element2 + "=?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setObject(1, value1);
		ps.setObject(2, value2);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			user.setCommentId(rs.getLong("commentId"));
			user.setName(rs.getString("name"));
			user.setArticleId(rs.getLong("articleId"));
			user.setTopic(rs.getString("topic"));
			user.setComment(rs.getString("comment"));
			user.setUploadTime(rs.getString("uploadTime"));
			usercomment.put(user.getName(), user);
		}
		return usercomment;
	}

	@Override
	public List<Object> searchUsercomment(Long articleId, String userName) throws SQLException {
	    // 获取用户信息以获取name字段
	    UserinfoDao userinfoDao = new UserinfoDaoImpl();
	    List<Userinfo> userinfoList = userinfoDao.selectPerSonalInfo(userName);
	    Userinfo userinfo = userinfoList.get(0);
	    String name = userinfo.getName();
	    
	    DBUtil dbUtil = new DBUtil();
	    List<Object> usercommentList = new ArrayList<>();
	    
	    // 直接执行查询，不需要存在性判断
	    String sql = "SELECT * FROM c_usercomment WHERE articleId=? AND name=?";
	    PreparedStatement ps = dbUtil.getPreparedStatement(sql);
	    ps.setLong(1, articleId);
	    ps.setString(2, name);
	    ResultSet rs = ps.executeQuery();

	    // 遍历结果集并封装到Usercomment对象
	    while (rs.next()) {
	        Usercomment usercomment = new Usercomment();
	        usercomment.setCommentId(rs.getLong("commentId"));
	        usercomment.setName(rs.getString("name"));
	        usercomment.setArticleId(rs.getLong("articleId"));
	        usercomment.setTopic(rs.getString("topic"));
	        usercomment.setComment(rs.getString("comment"));
	        usercomment.setUploadTime(rs.getString("uploadTime"));
	        usercommentList.add(usercomment);
	    }
	    
	    return usercommentList;
	}

	@Override
	public List<Object> searchUsercomment(Long articleId) throws SQLException {
	    // 获取用户信息以获取name字段
	    DBUtil dbUtil = new DBUtil();
	    List<Object> usercommentList = new ArrayList<>();
	    
	    // 直接执行查询，不需要存在性判断
	    String sql = "SELECT * FROM c_usercomment WHERE articleId=?";
	    PreparedStatement ps = dbUtil.getPreparedStatement(sql);
	    ps.setLong(1, articleId);
	    ResultSet rs = ps.executeQuery();

	    // 遍历结果集并封装到Usercomment对象
	    while (rs.next()) {
	        Usercomment usercomment = new Usercomment();
	        usercomment.setCommentId(rs.getLong("commentId"));
	        usercomment.setName(rs.getString("name"));
	        usercomment.setArticleId(rs.getLong("articleId"));
	        usercomment.setTopic(rs.getString("topic"));
	        usercomment.setComment(rs.getString("comment"));
	        usercomment.setUploadTime(rs.getString("uploadTime"));
	        usercommentList.add(usercomment);
	    }
	    
	    return usercommentList;
	}

	@Override
	public List<Object> SelectComment(int page, int limit) throws SQLException {
		List<Object> commentList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT * FROM c_usercomment LIMIT ?,?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setInt(1, (page - 1) * limit);
		ps.setInt(2, limit);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Usercomment userComment = new Usercomment();
			userComment.setCommentId(rs.getLong("commentId"));
			userComment.setName(rs.getString("name"));
			userComment.setArticleId(rs.getLong("articleId"));
			userComment.setTopic(rs.getString("topic"));
			userComment.setComment(rs.getString("comment"));
			userComment.setUploadTime(rs.getString("uploadTime"));
			commentList.add(userComment);
		}
		return commentList;
	}

	@Override
	public int CountComment() throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT count(*) as sum FROM c_usercomment";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getInt("sum");
		}
		return 0;
	}
	
	@Override
	public int SubmitComment(String userName, long articleId, String comment) throws SQLException {
	    DBUtil dbUtil = new DBUtil();
	    PreparedStatement ps = null;

	    // 1. 查询用户信息获取用户名（name）
	    UserinfoDao userinfoDao=new UserinfoDaoImpl();
	    Map<String, Userinfo> userMap = userinfoDao.searchUserInfo("userName", userName);
	    if (userMap == null || userMap.isEmpty()) {
	        return -1; // 用户不存在
	    }
	    Userinfo user = userMap.get(userName);
	    String name = user.getName();

	    // 2. 查询文章信息获取标题（topic）
	    ArticleinfoDao articleinfoDao=new ArticleinfoDaoImpl();
	    Map<Long, Articleinfo> articleMap = articleinfoDao.searchArticleInfo("articleId", articleId);
	    if (articleMap == null || articleMap.isEmpty()) {
	        return -2; // 文章不存在
	    }
	    Articleinfo article = articleMap.get(articleId);
	    String topic = article.getTopic();

	    // 3. 执行插入操作（不包含commentId和uploadTime，依赖表结构自动生成）
	    String sql = "INSERT INTO c_usercomment " +
	                 "(name, articleId, topic, comment) " +
	                 "VALUES (?, ?, ?, ?);";
	    ps = dbUtil.getPreparedStatement(sql);
	    ps.setString(1, name);          // 用户名
	    ps.setLong(2, articleId);       // 文章ID
	    ps.setString(3, topic);         // 文章标题
	    ps.setString(4, comment);       // 评论内容

	    return ps.executeUpdate();
	}
	
	@Override
	public List<Object> SearchComment(Usercomment usercomment, int page, int limit) throws SQLException {
		List<Object> searchCommentList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		// 使用StringBuilder构建SQL查询语句，初始语句为查询所有记录的基础部分，并添加WHERE 1=1方便后续拼接条件
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM c_usercomment WHERE 1=1");
		long articleId=usercomment.getArticleId();
		String topic = usercomment.getTopic();
		String name = usercomment.getName();
		List<Object> params = new ArrayList<>();

		// 如果文章ID不为-1，说明有文章ID的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (articleId != -1) {
			sqlBuilder.append(" AND CAST(articleId AS CHAR) LIKE ?");
			params.add("%" + articleId + "%");
		}
		// 如果文章标题不为null，说明有文章标题的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (topic != null) {
			sqlBuilder.append(" AND topic LIKE ?");
			params.add("%" + topic + "%");
		}
		// 如果文章作者不为null，说明有文章作者的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (name != null) {
			sqlBuilder.append(" AND name LIKE ?");
			params.add("%" + name + "%");
		}

		// 添加分页查询的LIMIT子句到SQL语句中，并将分页参数值添加到params列表
		sqlBuilder.append(" LIMIT ?,?");
		params.add((page - 1) * limit);
		params.add(limit);

		// 将StringBuilder中的内容转换为完整的SQL语句字符串
		String sql = sqlBuilder.toString();

		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		// 遍历params列表，为预编译语句的占位符设置对应的参数值
		for (int i = 0; i < params.size(); i++) {
			ps.setObject(i + 1, params.get(i));
		}

		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Usercomment searchComment = new Usercomment();
			searchComment.setCommentId(rs.getLong("commentId"));
			searchComment.setName(rs.getString("name"));
			searchComment.setArticleId(rs.getLong("articleId"));
			searchComment.setTopic(rs.getString("topic"));
			searchComment.setComment(rs.getString("comment"));
			searchComment.setUploadTime(rs.getString("uploadTime"));
			searchCommentList.add(searchComment);
		}
		return searchCommentList;
	}

	@Override
	public int CountComment(Usercomment usercomment) throws SQLException {
		int row = 0;
		DBUtil dbUtil = new DBUtil();
		// 使用StringBuilder构建SQL查询语句，初始语句为查询所有记录的基础部分，并添加WHERE 1=1方便后续拼接条件
		StringBuilder sqlBuilder = new StringBuilder("SELECT count(*) as sum FROM c_usercomment WHERE 1=1");
		long articleId=usercomment.getArticleId();
		String topic = usercomment.getTopic();
		String name = usercomment.getName();
		List<Object> params = new ArrayList<>();

		// 如果文章ID不为-1，说明有文章ID的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (articleId != -1) {
			sqlBuilder.append(" AND CAST(articleId AS CHAR) LIKE ?");
			params.add("%" + articleId + "%");
		}
		// 如果文章标题不为null，说明有文章标题的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (topic != null) {
			sqlBuilder.append(" AND topic LIKE ?");
			params.add("%" + topic + "%");
		}
		// 如果文章作者不为null，说明有文章作者的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (name != null) {
			sqlBuilder.append(" AND name LIKE ?");
			params.add("%" + name + "%");
		}

		// 将StringBuilder中的内容转换为完整的SQL语句字符串
		String sql = sqlBuilder.toString();

		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		for (int i = 0; i < params.size(); i++) {
			ps.setObject(i + 1, params.get(i));
		}

		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			row = rs.getInt("sum");
		}
		return row;
	}
	
	@Override
	public int deleteComment(Long commentId) throws SQLException {
		// TODO Auto-generated method stub
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		// SQL删除语句
		String sql = "DELETE FROM c_usercomment WHERE commentId = ?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setLong(1, commentId);
		// 执行SQL删除操作，并获取受影响的行数
		// 1 删除成功,0 删除失败
		int row = ps.executeUpdate();
		return row;
	}

	@Override
	public List<Object> SelectMyComment(String userName) throws SQLException {
		UserinfoDao userinfoDao=new UserinfoDaoImpl();
	    Map<String, Userinfo> userMap = userinfoDao.searchUserInfo("userName", userName);
	    if (userMap == null || userMap.isEmpty()) {
	        return new ArrayList<>(); 
	    }
	    String name = userMap.get(userName).getName();
	    if (name == null) {
	        return new ArrayList<>();
	    }
		
		List<Object> myCommentList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT * FROM c_usercomment where name=?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, name);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Usercomment userComment = new Usercomment();
			userComment.setCommentId(rs.getLong("commentId"));
			userComment.setName(rs.getString("name"));
			userComment.setArticleId(rs.getLong("articleId"));
			userComment.setTopic(rs.getString("topic"));
			userComment.setComment(rs.getString("comment"));
			userComment.setUploadTime(rs.getString("uploadTime"));
			myCommentList.add(userComment);
		}
		return myCommentList;
	}

	@Override
	public int CountMyComment(String userName) throws SQLException {
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
		String sql = "SELECT count(*) as sum FROM c_usercomment where name=?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, name);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			return rs.getInt("sum");
		}
		return 0;
	}
}
