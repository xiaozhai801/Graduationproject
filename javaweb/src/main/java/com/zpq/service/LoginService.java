package com.zpq.service;

import java.sql.SQLException;

import com.zpq.pojo.Admin;
import com.zpq.pojo.User;

public interface LoginService {
	Admin selectAdmin(String name,String password) throws SQLException;
	User selectUser(String name,String password) throws SQLException;
}
