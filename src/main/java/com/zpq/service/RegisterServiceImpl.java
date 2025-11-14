package com.zpq.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.zpq.dao.UserinfoDao;
import com.zpq.dao.UserinfoDaoImpl;
import com.zpq.pojo.Vo;

public class RegisterServiceImpl implements RegisterService {
    private UserinfoDao userDao = new UserinfoDaoImpl();

    @Override
    public Vo addUser(String userName, String password) throws SQLException {
        Vo vo = new Vo();
        vo.setCount(0);
        vo.setData(Collections.emptyList());

        String daoResult = userDao.addUser(userName, password);

        if ("user exists".equals(daoResult)) {
            vo.setCode(1);
            vo.setMsg("user exists");
        } else if ("success".equals(daoResult)) {
            // 注册成功
            vo.setCode(0);
            vo.setMsg("success");
            vo.setCount(1);
            vo.setData(List.of(userName));
        } else {
            vo.setCode(2);
            vo.setMsg("fail");
        }

        return vo;
    }
}