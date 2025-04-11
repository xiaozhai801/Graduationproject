package com.zpq.dao;

import java.sql.SQLException;
//只能添加用户，管理员只有1位
public interface UserAddDao {
	String addUser(String name,String password) throws SQLException;
}
