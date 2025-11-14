package com.zpq.controller;
// 删除用户 1
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

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserInfoService userInfoService = new UserInfoServiceImpl();

    public DeleteUserServlet() {
        super();
    }

    private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 获取参数和Cookie中的角色
        String userNames = getParamValue(request, "userNames", "");
        int role = 1;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("role".equals(cookie.getName())) {
                    role = Integer.parseInt(cookie.getValue());
                }
            }
        }

        // 调用Service层处理删除逻辑
        Vo vo;
        try {
            vo = userInfoService.deleteUsers(userNames, role);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doGet(request, response);
    }
}