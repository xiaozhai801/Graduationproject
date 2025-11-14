package com.zpq.controller;
// 发布文章审核 1
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.ArticleService;
import com.zpq.service.ArticleServiceImpl;

@WebServlet("/UploadArticleReviewServlet")
public class UploadArticleReviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ArticleService articleService = new ArticleServiceImpl();

    public UploadArticleReviewServlet() {
        super();
    }

    // 获取请求参数，提供默认值
    private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8"); // 统一JSON响应

        // 从Cookie获取当前用户名
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

        // 获取请求参数
        String articleIdStr = getParamValue(request, "articleId", articleService.generateArticleId()); // 无ID时自动生成
        long articleId = Long.parseLong(articleIdStr); // 用long避免int溢出
        int status = Integer.parseInt(getParamValue(request, "status", "2")); // 发布状态（1=直接发布）
        // 直接发布时需额外参数
        String topic = getParamValue(request, "topic", null);
        String dataHtml = getParamValue(request, "contentHtml", "");
        int typeId = Integer.parseInt(getParamValue(request, "typeId", "-1"));

        // 调用Service层提交审核，获取响应结果
        Vo vo;
        try {
            vo = articleService.publishArticleForReview(userName, articleId, status, topic, dataHtml, typeId);
        } catch (Exception e) {
            e.printStackTrace();
            // 系统异常处理
            vo = new Vo();
            vo.setCode(500);
            vo.setMsg("系统错误：" + e.getMessage());
            vo.setCount(0);
            vo.setData(null);
        }

        // 响应JSON数据
        response.getWriter().write(JSONObject.toJSONString(vo));
    }
}