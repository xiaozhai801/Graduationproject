package com.zpq.service;

import java.sql.SQLException;

import com.zpq.dao.UserAddDao;
import com.zpq.dao.UserAddDaoImpl;

public class RegisterServiceImpl implements RegisterService {
	private UserAddDao userAddDao=new UserAddDaoImpl();
	@Override
	public String addUser(String name, String password) throws SQLException {
		// TODO Auto-generated method stub
		return userAddDao.addUser(name,password);
	}

}
