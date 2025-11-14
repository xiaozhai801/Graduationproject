package com.zpq.service;

import java.sql.SQLException;
import com.zpq.pojo.Vo;

public interface RegisterService {
	// 添加用户
    Vo addUser(String userName, String password) throws SQLException;
}