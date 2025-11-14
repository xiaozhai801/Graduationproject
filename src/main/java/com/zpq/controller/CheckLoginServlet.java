package com.zpq.controller;
// 检查登录 1
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.LoginService;
import com.zpq.service.LoginServiceImpl;

@WebServlet("/CheckLoginServlet")
public class CheckLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginService loginService = new LoginServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取请求参数
        String userName = request.getParameter("name");
        String password = request.getParameter("password");
        response.setContentType("application/json;charset=UTF-8");

        try {
            Vo vo = loginService.selectUser(userName, password);
            
            if (vo.getCode() == 0) {
                Cookie usernameCookie = new Cookie("userName", userName);
                usernameCookie.setPath("/");
                usernameCookie.setMaxAge(99999);
                response.addCookie(usernameCookie);

                // 设置角色Cookie（从返回结果中获取角色）
                String role = (String) vo.getData().get(0);
                Cookie roleCookie = new Cookie("role", role);
                roleCookie.setPath("/");
                roleCookie.setMaxAge(99999);
                response.addCookie(roleCookie);
            }

            response.getWriter().write(JSONObject.toJSONString(vo));

        } catch (SQLException e) {
            e.printStackTrace();
            Vo errorVo = new Vo();
            errorVo.setCode(500);
            errorVo.setMsg("系统错误，请稍后重试");
            response.getWriter().write(JSONObject.toJSONString(errorVo));
        }
    }
}