package com.zpq.controller;
// 查询所有文章 1
import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.ArticleService;
import com.zpq.service.ArticleServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/SelectAllArticleServlet")
public class SelectAllArticleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ArticleService articleService = new ArticleServiceImpl();

    public SelectAllArticleServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));

        Vo vo;
        try {
            vo = articleService.getAllAuditArticles(page, limit);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}