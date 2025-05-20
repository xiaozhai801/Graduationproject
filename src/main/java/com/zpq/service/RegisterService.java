package com.zpq.service;

import java.sql.SQLException;

public interface RegisterService {
	String addUser(String name,String password) throws SQLException;
}
