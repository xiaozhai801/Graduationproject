package com.zpq.dao;

import java.sql.SQLException;

import com.zpq.pojo.User;

public interface EditPersonalInfoDao {
	public int EditUser(User user) throws SQLException;
}
