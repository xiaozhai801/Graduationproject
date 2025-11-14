package com.zpq.controller;
// 显示当前文章内容 1 
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.ArticleService;
import com.zpq.service.ArticleServiceImpl;

@WebServlet("/ShowArticleServlet")
public class ShowArticleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ArticleService articleService = new ArticleServiceImpl();

    public ShowArticleServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8"); // 统一JSON响应

        // 获取请求参数（文章ID）
        long articleId = Long.parseLong(request.getParameter("articleId"));

        // 调用Service层获取文章内容
        Vo vo;
        try {
            vo = articleService.showArticleContent(articleId);
        } catch (Exception e) {
            e.printStackTrace();
            // 系统异常处理
            vo = new Vo();
            vo.setCode(500);
            vo.setMsg("系统错误：" + e.getMessage());
            vo.setCount(0);
            vo.setData(new ArrayList<>());
        }

        // 响应JSON数据
        response.getWriter().write(JSONObject.toJSONString(vo));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Post请求复用Get逻辑（与原代码一致）
        doGet(request, response);
    }
}