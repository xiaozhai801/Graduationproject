package com.zpq.service;

import com.zpq.dao.UserinfoDao;
import com.zpq.dao.UserinfoDaoImpl;
import com.zpq.pojo.Userinfo;
import com.zpq.pojo.Vo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoServiceImpl implements UserInfoService {
    private UserinfoDao userDao = new UserinfoDaoImpl();

    @Override
    public Vo updatePassword(String userName, String oldPassword, String newPassword) throws SQLException {
        Vo vo = new Vo();
        Userinfo user = new Userinfo();
        user.setUserName(userName);
        user.setPassword(oldPassword);

        int result = userDao.editPersonalPassword(user, newPassword);

        if (result == 1) {
            vo.setCode(0);
            vo.setMsg("success");
        } else {
            vo.setCode(1);
            vo.setMsg("fail");
        }
        vo.setCount(0);
        vo.setData(new ArrayList<>());
        return vo;
    }

    @Override
    public Vo getPersonalInfo(String userName, int role) throws SQLException {
        Vo vo = new Vo();
        List<Userinfo> userList = userDao.selectPerSonalInfo(userName);

        List<Object> newUserList = new ArrayList<>();
        for (Object userObj : userList) {
            if (userObj instanceof Userinfo) {
                Userinfo user = (Userinfo) userObj;
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("role", role);
                userMap.put("userName", user.getUserName());
                userMap.put("name", user.getName());
                userMap.put("password", user.getPassword());
                userMap.put("sex", user.getSex());
                userMap.put("age", user.getAge());
                userMap.put("email", user.getEmail());
                userMap.put("avatar", user.getAvatar());
                newUserList.add(userMap);
            }
        }

        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(newUserList.size());
        vo.setData(newUserList);
        return vo;
    }

    @Override
    public Vo updatePersonalInfo(Userinfo user) throws SQLException {
        Vo vo = new Vo();
        int result = userDao.editPersonalInfo(user);

        if (result == 1) {
            vo.setCode(0);
            vo.setMsg("success");
        } else {
            vo.setCode(1);
            vo.setMsg("fail");
        }
        vo.setCount(0);
        vo.setData(new ArrayList<>());
        return vo;
    }
    
    @Override
    public Vo deleteUsers(String userNames, int role) throws SQLException {
        Vo vo = new Vo();
        vo.setCount(0);
        vo.setData(new ArrayList<>());

        // 校验权限（假设role=0为管理员，有权删除）
        if (role != 0) {
            vo.setCode(2);
            vo.setMsg("fail");
            return vo;
        }

        // 处理用户名单（分割、去空）
        if (userNames == null || userNames.isEmpty()) {
            vo.setCode(3);
            vo.setMsg("fail");
            return vo;
        }
        String[] userNameArr = userNames.split(",");
        int deleteCount = 0;

        // 批量删除
        for (String userName : userNameArr) {
            if (!userName.isEmpty()) {
                deleteCount += userDao.deleteUser(userName);
            }
        }

        // 封装结果
        if (deleteCount > 0) {
            vo.setCode(0);
            vo.setMsg("success");
        } else {
            vo.setCode(1);
            vo.setMsg("fail");
        }
        return vo;
    }
    
    @Override
    public Vo addUser(Userinfo user, int operatorRole) throws SQLException {
        Vo vo = new Vo();
        vo.setCount(0);
        vo.setData(new ArrayList<>());

        // 权限校验：仅管理员（role=0）可添加用户
        if (operatorRole != 0) {
            vo.setCode(2);
            vo.setMsg("fail");
            return vo;
        }

        // 参数校验：用户名和密码不能为空
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            vo.setCode(3);
            vo.setMsg("fail");
            return vo;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            vo.setCode(4);
            vo.setMsg("fail");
            return vo;
        }

        // 检查用户名是否已存在
        if (userDao.checkUserExists(user.getUserName())) {
            vo.setCode(1);
            vo.setMsg("fail");
            return vo;
        }

        user.setRole(1); // 默认普通用户
        
        int result = userDao.addUser(user);

        // 封装结果
        if (result == 1) {
            vo.setCode(0);
            vo.setMsg("success");
        } else {
            vo.setCode(5);
            vo.setMsg("fail");
        }
        return vo;
    }
    
    @Override
    public Vo getAllUsers(int page, int limit) throws SQLException {
        Vo vo = new Vo();
        // 1. 调用Dao层获取原始用户列表
        List<Object> userList = userDao.SelectAllUserLsit(page, limit);
        // 2. 处理数据：排除管理员、添加editable属性
        List<Object> newUserList = new ArrayList<>();
        for (Object userObj : userList) {
            if (userObj instanceof Userinfo) {
                Userinfo user = (Userinfo) userObj;
                // 排除管理员用户
                if ("管理员".equals(user.getName())) {
                    continue;
                }
                // 封装用户数据（添加editable属性）
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userName", user.getUserName());
                userMap.put("name", user.getName());
                userMap.put("password", user.getPassword());
                userMap.put("sex", user.getSex());
                userMap.put("age", user.getAge());
                userMap.put("email", user.getEmail());
                userMap.put("editable", true); // 支持编辑标识
                newUserList.add(userMap);
            }
        }
        // 3. 封装Vo对象
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(userDao.countUser()); // 总用户数（从Dao层获取）
        vo.setData(newUserList); // 处理后的用户列表
        return vo;
    }

	@Override
	public String getWebName(String userName) throws SQLException {
		// TODO Auto-generated method stub
		return userDao.getWebName(userName);
	}
    

}