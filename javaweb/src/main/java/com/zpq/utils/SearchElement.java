package com.zpq.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.zpq.pojo.Article;
import com.zpq.pojo.Model;
import com.zpq.pojo.User;

public class SearchElement {
	//查询用户信息
	public User searchUserInfo(String element,String value) throws SQLException {
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		User user = new User();
		String sql = "SELECT * FROM v_userinfo where "+element+"=?";
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
	//查询文章信息
	public Article searchArticleInfo(String element,String value) throws SQLException {
		DBUtil dbUtil = new DBUtil();
		Article article = new Article();
		String sql = "SELECT * FROM v_articleinfo where "+element+"=?";
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
			article.setData(rs.getString("data"));
		}
		// 返回查询到的文章对象列表
		return article;
	}
	//查询型号列表
	public Model searchModelInfo(String element,String value) throws SQLException {
		DBUtil dbUtil = new DBUtil();
		Model model = new Model();
		String sql = "SELECT * FROM v_modeltype where "+element+"=?";
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
}
