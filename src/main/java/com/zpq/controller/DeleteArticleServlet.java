package com.zpq.controller;
// 删除文章 1
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.ArticleService;
import com.zpq.service.ArticleServiceImpl;

@WebServlet("/DeleteArticleServlet")
public class DeleteArticleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ArticleService articleService = new ArticleServiceImpl();

    public DeleteArticleServlet() {
        super();
    }

    private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8"); // 响应JSON格式

        // 获取参数
        String articleIdStr = getParamValue(request, "articleId", "-1");
        int articleId = Integer.parseInt(articleIdStr);

        Vo vo = new Vo();
        try {
            boolean isSuccess;
            isSuccess = articleService.deleteAdminArticle(articleId);
            
            // 设置Vo响应信息
            if (isSuccess) {
                vo.setCode(0);
                vo.setMsg("success");
            } else {
                vo.setCode(1);
                vo.setMsg("fail");
            }
            vo.setCount(0); // 删除操作无数据量，默认为0
            vo.setData(null); // 无数据列表，设为null
        } catch (Exception e) {
            e.printStackTrace();
            // 系统异常时的响应
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