package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;
import com.zpq.pojo.Admin;
import com.zpq.pojo.User;
import com.zpq.pojo.UserComment;

public interface UserDao {
	// 更新文章列表信息
	public void Updateinformation(int titleId) throws SQLException;

	// 查询管理员列表
	public Admin selectAdmin(String name, String password) throws SQLException;

	// 查询用户列表
	public User selectUser(String name, String password) throws SQLException;

	// 带分页查询
	public List<Object> SelectAllUserLsit(int page, int limit) throws SQLException;

	// 用户数量
	public int countUser() throws SQLException;

	// 添加用户
	String addUser(String name, String password) throws SQLException;

	// 用户信息编辑功能
	public int EditUser(User user) throws SQLException;

	// 修改用户密码
	public int EditPersonalPassword(User user, String newPassword) throws SQLException;

	// 用户点赞文章
	public int Thumbsup(long id, String userId, int titleId, boolean like) throws SQLException;

	// 用户收藏文章
	public int Collect(long id, String userId, int titleId, boolean favorite) throws SQLException;

	// 用户提交评论
	public int SubmitComment(long id, String userId, int titleId, String comment) throws SQLException;

	// 查询当前用户收藏文章
	public List<Object> SelectMyFavorite(String userId) throws SQLException;

	// 当前用户收藏文章数量
	public int CountMyFavorite(String userId) throws SQLException;

	// 查询当前用户点赞文章
	public List<Object> SelectMyLike(String userId) throws SQLException;

	// 当前用户点赞文章数量
	public int CountMyLike(String userId) throws SQLException;

	// 当前用户评论信息
	public List<Object> SelectMyComment(String userId) throws SQLException;

	// 当前用户评论数量
	public int CountMyComment(String userId) throws SQLException;

	// 所有评论信息,带分页
	public List<Object> SelectComment(int page, int limit) throws SQLException;

	// 所有评论数量
	public int CountComment() throws SQLException;

	// 搜索评论信息,带分页
	public List<Object> SearchComment(UserComment userComment, int page, int limit) throws SQLException;
	
	// 搜索条件后所有评论数量
	public int CountComment(UserComment userComment) throws SQLException;
}