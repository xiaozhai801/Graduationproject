package com.zpq.controller;
// 保存文章（草稿）1
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

@WebServlet("/SaveArticleServlet")
public class SaveArticleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ArticleService articleService = new ArticleServiceImpl();

    public SaveArticleServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    // 工具方法：获取请求参数，提供默认值
    private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8"); // 统一JSON响应

        // 从Cookie获取当前用户名（校验登录状态）
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
        if (userName == null) {
            Vo vo = new Vo();
            vo.setCode(7);
            vo.setMsg("请先登录");
            vo.setCount(0);
            vo.setData(new ArrayList<>());
            response.getWriter().write(JSONObject.toJSONString(vo));
            return;
        }

        // 获取请求参数（草稿信息）
        String topic = getParamValue(request, "topic", null); // 文章标题
        String dataHtml = getParamValue(request, "contentHtml", ""); // HTML内容
        int typeId = Integer.parseInt(getParamValue(request, "typeId", "-1")); // 文章类型ID

        // 调用Service层保存草稿，获取响应结果
        Vo vo;
        try {
            // 无需在Controller生成文章ID，Service层已实现ID生成逻辑
            vo = articleService.saveDraftArticle(userName, topic, dataHtml, typeId);
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
}