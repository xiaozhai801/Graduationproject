package com.zpq.controller;
//查询所有用户 1
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.UserInfoService;
import com.zpq.service.UserInfoServiceImpl;

@WebServlet("/SelectAllUserServlet")
public class SelectAllUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserInfoService userInfoService = new UserInfoServiceImpl();

    public SelectAllUserServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应格式（JSON+UTF-8）
        response.setContentType("application/json;charset=UTF-8");

        // 获取分页参数（page=当前页，limit=每页条数）
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));

        // 调用Service层处理业务，获取Vo结果
        Vo vo = new Vo();
        try {
            vo = userInfoService.getAllUsers(page, limit);
        } catch (Exception e) {
            e.printStackTrace();
            // 异常时封装错误响应
            vo.setCode(500);
            vo.setMsg("系统错误：" + e.getMessage());
            vo.setCount(0);
            vo.setData(null);
        }

        // 响应JSON数据
        response.getWriter().write(JSONObject.toJSONString(vo));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}