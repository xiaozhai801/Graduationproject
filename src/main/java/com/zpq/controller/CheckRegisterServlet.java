package com.zpq.controller;
// 检查注册 1

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.RegisterService;
import com.zpq.service.RegisterServiceImpl;
@WebServlet("/CheckRegisterServlet")
public class CheckRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RegisterService registerService = new RegisterServiceImpl();

    public CheckRegisterServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String userName = request.getParameter("name");
        String password = request.getParameter("password");

        try {
            Vo vo = registerService.addUser(userName, password);

            response.getWriter().write(JSONObject.toJSONString(vo));
        } catch (SQLException e) {
            e.printStackTrace();

            Vo errorVo = new Vo();
            errorVo.setCode(500);
            errorVo.setMsg("系统错误，请稍后重试");
            errorVo.setCount(0);
            errorVo.setData(Collections.emptyList());
            response.getWriter().write(JSONObject.toJSONString(errorVo));
        }
    }
}