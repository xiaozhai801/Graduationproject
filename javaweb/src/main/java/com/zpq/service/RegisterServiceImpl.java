package com.zpq.service;

import java.sql.SQLException;

import com.zpq.dao.UserAddDao;
import com.zpq.dao.UserAddDaoImpl;
import com.zpq.dao.UserDao;
import com.zpq.dao.UserDaoImpl;
import com.zpq.pojo.User;

public class RegisterServiceImpl implements RegisterService {
	private UserAddDao userAddDao=new UserAddDaoImpl();
	@Override
	public String addUser(String name, String password) throws SQLException {
		// TODO Auto-generated method stub
		return userAddDao.addUser(name,password);
	}

}
