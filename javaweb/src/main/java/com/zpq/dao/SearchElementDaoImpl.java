package com.zpq.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.zpq.pojo.Article;
import com.zpq.pojo.Draft;
import com.zpq.pojo.Model;
import com.zpq.pojo.User;
import com.zpq.pojo.UserAction;
import com.zpq.utils.DBUtil;

public class SearchElementDaoImpl implements SearchElementDao {

	@Override
	public Map<String, User> searchUserInfo(String element, String value) throws SQLException {
		// TODO Auto-generated method stub
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		User user = new User();
		Map<String, User> userInfo=new HashMap<>();
		String sql = "SELECT * FROM v_userinfo where " + element + "=?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			user.setUserId(rs.getString("userId"));
			user.setName(rs.getString("name"));
			user.setSex(rs.getString("sex"));
			user.setAge(rs.getInt("age"));
			user.setEmail(rs.getString("email"));
			userInfo.put(user.getUserId(), user);
		}
		return userInfo;
	}

	@Override
	public Map<Integer, Article> searchArticleInfo(String element, String value) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		Article article = new Article();
		Map<Integer, Article> articleInfo=new HashMap<>();
		String sql = "SELECT * FROM v_articleinfo where " + element + "=?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			article.setTitleId(rs.getInt("titleId"));
			article.setUserId(rs.getString("userId"));
			article.setName(rs.getString("name"));
			article.setTopic(rs.getString("topic"));
			article.setModel(rs.getString("model"));
			article.setUploadTime(rs.getString("uploadTime"));
			article.setRelease(rs.getInt("release"));
			article.setViews(rs.getInt("views"));
			article.setLikes(rs.getInt("likes"));
			article.setFavorites(rs.getInt("favorites"));
			article.setData_html(rs.getString("data_html"));
			article.setData_text(rs.getString("data_text"));
			articleInfo.put(article.getTitleId(), article);
		}
		// 返回查询到的文章对象列表
		return articleInfo;
	}

	@Override
	public Map<Integer, Model> searchModelInfo(String element, String value) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		Model model = new Model();
		Map<Integer, Model> modelInfo=new HashMap<>();
		String sql = "SELECT * FROM v_modeltype where " + element + "=?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			model.setTypeId(rs.getInt("typeId"));
			model.setTypeName(rs.getString("typeName"));
			modelInfo.put(model.getTypeId(), model);
		}
		// 返回查询到的型号对象列表
		return modelInfo;
	}

	@Override
	public Map<Integer, Draft> searchDraftInfo(Object element, Object value) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		Draft draft = new Draft();
		Map<Integer, Draft> draftInfo=new HashMap<>();
		String sql = "SELECT * FROM v_articledraft where " + element + "=?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setObject(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			draft.setDraftId(rs.getInt("draftId"));
			draft.setTopic(rs.getString("topic"));
			draft.setUserId(rs.getString("userId"));
			draft.setName(rs.getString("name"));
			draft.setModel(rs.getString("model"));
			draft.setTypeId(rs.getInt("typeId"));
			draft.setData_html(rs.getString("data_html"));
			draft.setData_text(rs.getString("data_text"));
			draftInfo.put(draft.getDraftId(), draft);
		}
		// 返回查询到的型号对象列表
		return draftInfo;
	}

	@Override
	public Map<Long, UserAction> searchUserActionInfo(String userId,int titleId) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		UserAction userAction=new UserAction();
		Map<Long, UserAction> userActionInfo=new HashMap<>();
		long id=(long) titleId*10+Integer.parseInt(userId);

		// 初始化用户行为
		String initSql = "INSERT INTO c_useractions (id,userId, titleId) VALUES (?,?,?) ON DUPLICATE KEY UPDATE userId = VALUES(userId),titleId = VALUES(titleId);";
		PreparedStatement ps = dbUtil.getPreparedStatement(initSql);		// 获取预编译的SQL语句对象
		ps.setObject(1, id);		// 执行ID
		ps.setObject(2, userId);		// 用户ID
		ps.setObject(3, titleId);		// 文章ID
		ps.executeUpdate();

		// 查询用户行为
		String selectSql="select id,userId,titleId,`like`,favorite from c_useractions where userId=? and titleId=?";
		ps = dbUtil.getPreparedStatement(selectSql);
		ps.setObject(1, userId);		// 用户ID
		ps.setObject(2, titleId);		// 文章ID
		ResultSet rs=ps.executeQuery();
		while (rs.next()) {
			userAction.setId(rs.getLong("id"));
			userAction.setUserId(rs.getString("userId"));
			userAction.setTitleId(rs.getInt("titleId"));
			userAction.setLike(rs.getBoolean("like"));
			userAction.setFavorite(rs.getBoolean("favorite"));
			userActionInfo.put(userAction.getId(), userAction);
		}
		return userActionInfo;		// 返回查询到的用户行为
	}

}
