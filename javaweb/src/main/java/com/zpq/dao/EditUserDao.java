package com.zpq.dao;

import java.sql.SQLException;

import com.zpq.pojo.User;
//实现用户信息编辑功能
public interface EditUserDao {
	public int EditUser(User user) throws SQLException;
}
