package com.zpq.controller;
// 修改密码 1
import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.UserInfoService;
import com.zpq.service.UserInfoServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/EditPasswordServlet")
public class EditPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserInfoService userInfoService = new UserInfoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String userName = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userName".equals(cookie.getName())) {
                    userName = cookie.getValue();
                    break;
                }
            }
        }

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");

        Vo vo;
        try {
            vo = userInfoService.updatePassword(userName, oldPassword, newPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            vo = new Vo();
            vo.setCode(500);
            vo.setMsg("system error");
            vo.setCount(0);
            vo.setData(new ArrayList<>());
        }

        response.getWriter().write(JSONObject.toJSONString(vo));
    }
}