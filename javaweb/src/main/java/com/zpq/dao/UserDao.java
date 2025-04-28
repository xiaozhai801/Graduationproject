package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;
import com.zpq.pojo.Admin;
import com.zpq.pojo.User;

public interface UserDao {
	// 查询管理员
	public Admin selectAdmin(String name, String password) throws SQLException;

	// 查询用户
	public User selectUser(String name, String password) throws SQLException;

	// 带分页查询
	public List<Object> SelectAllUserLsit(int page, int limit) throws SQLException;

	public int countUser() throws SQLException;

	// 添加用户
	String addUser(String name, String password) throws SQLException;

	// 用户信息编辑功能
	public int EditUser(User user) throws SQLException;

	// 修改用户密码
	public int EditPersonalPassword(User user, String newPassword) throws SQLException;

	// 用户点赞文章
	public int Thumbsup(long id,String userId,int titleId,boolean like) throws SQLException;
	
	// 用户收藏文章
	public int Collect(long id,String userId,int titleId,boolean favorite) throws SQLException;
}