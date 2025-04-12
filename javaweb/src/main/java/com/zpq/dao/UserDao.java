package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;

import com.zpq.pojo.Admin;
import com.zpq.pojo.User;
//查询用户
public interface UserDao {
	public Admin selectAdmin(String name, String password) throws SQLException;
	public User selectUser(String name, String password) throws SQLException;
//    public List<Object> SelectAllUserLsit() throws SQLException;
	//带分页查询
    public List<Object> SelectAllUserLsit(int page,int limit) throws SQLException;
    public int countUser() throws SQLException;
}