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
import com.zpq.utils.DBUtil;

public class SearchElementDaoImpl implements SearchElementDao {

	@Override
	public User searchUserInfo(String element, String value) throws SQLException {
		// TODO Auto-generated method stub
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		User user = new User();
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
		}
		return user;
	}

	@Override
	public Article searchArticleInfo(String element, String value) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		Article article = new Article();
		String sql = "SELECT * FROM v_articleinfo where " + element + "=?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		// 遍历结果集，将每一条记录封装成Article对象并添加到articleList中
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
			article.setData_html(rs.getString("data_html"));
			article.setData_text(rs.getString("data_text"));

		}
		// 返回查询到的文章对象列表
		return article;
	}

	@Override
	public Model searchModelInfo(String element, String value) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		Model model = new Model();
		String sql = "SELECT * FROM v_modeltype where " + element + "=?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		// 遍历结果集，将每一条记录封装成Article对象并添加到articleList中
		while (rs.next()) {
			model.setTypeId(rs.getInt("typeId"));
			model.setTypeName(rs.getString("typeName"));
		}
		// 返回查询到的型号对象列表
		return model;
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
		// 遍历结果集，将每一条记录封装成Article对象并添加到articleList中
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
	public int countDraft(String element, String value) throws SQLException {
		// TODO Auto-generated method stub
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		// SQL语句，统计v_articleinfo表中的记录数量，使用count(*)函数，并将结果命名为sum
		String sql = "SELECT count(*) as sum FROM v_articledraft where "+element+"=?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		// 遍历结果集，获取统计的文章数量并返回
		while (rs.next()) {
			return rs.getInt("sum");
		}
		// 如果没有查询到结果，返回0
		return 0;
	}

}
