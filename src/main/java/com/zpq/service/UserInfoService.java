package com.zpq.service;

import com.zpq.pojo.Userinfo;
import com.zpq.pojo.Vo;
import java.sql.SQLException;

public interface UserInfoService {
    // 修改密码
    Vo updatePassword(String userName, String oldPassword, String newPassword) throws SQLException;

    // 查询个人信息
    Vo getPersonalInfo(String userName, int role) throws SQLException;

    // 编辑个人信息
    Vo updatePersonalInfo(Userinfo user) throws SQLException;

    // 批量删除用户
    Vo deleteUsers(String userNames, int role) throws SQLException;

    // 添加用户
    Vo addUser(Userinfo user, int operatorRole) throws SQLException;
    
    // 分页查询所有用户（排除管理员）
    Vo getAllUsers(int page, int limit) throws SQLException;
    
    // 获取网站名称
    String getWebName(String userName) throws SQLException;
}