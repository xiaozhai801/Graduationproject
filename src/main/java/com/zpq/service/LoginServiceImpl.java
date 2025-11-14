package com.zpq.service;

import java.sql.SQLException;
import java.util.Collections;
import com.zpq.dao.UserinfoDao;
import com.zpq.dao.UserinfoDaoImpl;
import com.zpq.pojo.Userinfo;
import com.zpq.pojo.Vo;

public class LoginServiceImpl implements LoginService {
    private UserinfoDao userDao = new UserinfoDaoImpl();

    @Override
    public Vo selectUser(String userName, String password) throws SQLException {
        Vo vo = new Vo();
        Userinfo user = userDao.selectUser(userName, password);

        if (user != null) { 
            String role = (user.getRole() == 0) ? "0" : "1";
            vo.setCode(0);
            vo.setMsg("success");
            vo.setData(Collections.singletonList(role));
        } else {
            vo.setCode(1);
            vo.setMsg("用户名或密码错误");
        }
        return vo;
    }
}