package com.zpq.service;

import java.sql.SQLException;

import com.zpq.dao.UserDao;
import com.zpq.dao.UserDaoImpl;
import com.zpq.pojo.Admin;
import com.zpq.pojo.User;

public class LoginServletImpl implements LoginService {
	private UserDao userDao=new UserDaoImpl();
	@Override
	public Admin selectAdmin(String name, String password) throws SQLException {
		// TODO Auto-generated method stub
		return userDao.selectAdmin(name, password);
	}

	@Override
	public User selectUser(String name, String password) throws SQLException {
		// TODO Auto-generated method stub
		return userDao.selectUser(name, password);
	}

}
