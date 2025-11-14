package com.zpq.controller;
// 添加用户 1

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Userinfo;
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
@WebServlet("/AddUserServlet")
public class AddUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserInfoService userInfoService = new UserInfoServiceImpl();

    public AddUserServlet() {
        super();
    }

    // 获取请求参数，为空时返回默认值
    private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // 设置编码和响应格式
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 从Cookie获取操作者角色（判断是否为管理员）
        int operatorRole = 1; // 默认普通用户
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("role".equals(cookie.getName())) {
                    operatorRole = Integer.parseInt(cookie.getValue());
                    break;
                }
            }
        }

        // 获取请求参数，封装为Userinfo对象
        Userinfo user = new Userinfo();
        user.setUserName(getParamValue(request, "userName", ""));
        user.setPassword(getParamValue(request, "password", ""));
        user.setName(getParamValue(request, "name", ""));
        user.setSex(getParamValue(request, "sex", ""));
        // 处理年龄（为空时设为0）
        String ageStr = getParamValue(request, "age", "0");
        user.setAge(Integer.parseInt(ageStr));
        user.setEmail(getParamValue(request, "email", ""));
        // 处理角色（仅管理员可设置角色，普通用户默认1）
        String roleStr = getParamValue(request, "role", "1");
        user.setRole(Integer.parseInt(roleStr));

        // 调用Service层处理添加逻辑
        Vo vo;
        try {
            vo = userInfoService.addUser(user, operatorRole);
        } catch (SQLException e) {
            e.printStackTrace();
            // 系统异常处理
            vo = new Vo();
            vo.setCode(500);
            vo.setMsg("system error");
            vo.setCount(0);
            vo.setData(new ArrayList<>());
        }

        // 返回JSON响应
        response.getWriter().write(JSONObject.toJSONString(vo));
    }
}