package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.zpq.pojo.Userinfo;

public interface UserinfoDao {
	// 查询用户列表 1
	public Userinfo selectUser(String userName, String password) throws SQLException;

//	// 更新文章列表信息
//	public void Updateinformation(long articleId) throws SQLException;

	// 带分页查询
	public List<Object> SelectAllUserLsit(int page, int limit) throws SQLException;

	// 用户数量
	public int countUser() throws SQLException;

	// 添加用户
	public String addUser(String userName, String password) throws SQLException;

	// 管理员添加用户
	public int addUser(Userinfo user) throws SQLException;

	// 检查用户是否存在
	public boolean checkUserExists(String userName) throws SQLException;

	// 删除用户
	public int deleteUser(String userName) throws SQLException;

//	// 用户信息编辑功能
//	public int EditUser(User user) throws SQLException;
//
	// 修改用户密码
	public int editPersonalPassword(Userinfo user, String newPassword) throws SQLException;

	// 显示个人信息
	public List<Userinfo> selectPerSonalInfo(String userName) throws SQLException;

	// 编辑个人信息
	public int editPersonalInfo(Userinfo user) throws SQLException;

	// 查询用户信息,userName:Userinfo
	public Map<String, Userinfo> searchUserInfo(String element, String value) throws SQLException;
	
	// 通过用户名获取网站名称
	public String getWebName(String userName) throws SQLException;
}