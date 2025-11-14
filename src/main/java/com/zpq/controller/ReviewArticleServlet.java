package com.zpq.controller;
// 审核通过和不通过 1
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.ArticleService;
import com.zpq.service.ArticleServiceImpl;

@WebServlet("/ReviewArticleServlet")
public class ReviewArticleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ArticleService articleService = new ArticleServiceImpl();

    public ReviewArticleServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8"); // 统一JSON响应

        // 获取请求参数（文章ID+审核状态）
        long articleId = Long.parseLong(request.getParameter("articleId"));
        int passCode = Integer.parseInt(request.getParameter("passCode"));

        // 从Cookie获取操作者角色（默认普通用户role=1，无审核权限）
        int operatorRole = 1;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("role".equals(cookie.getName())) {
                    operatorRole = Integer.parseInt(cookie.getValue());
                    break;
                }
            }
        }

        // 调用Service层执行审核，获取结果
        Vo vo;
        try {
            vo = articleService.reviewArticle(articleId, passCode, operatorRole);
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
        doGet(request, response);
    }
}