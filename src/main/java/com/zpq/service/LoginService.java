package com.zpq.service;

import java.sql.SQLException;
import com.zpq.pojo.Vo;

public interface LoginService {
	// 查询所有用户
    Vo selectUser(String userName, String password) throws SQLException;
}