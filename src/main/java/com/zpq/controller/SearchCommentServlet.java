package com.zpq.controller;
// 搜索评论 1
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Usercomment;
import com.zpq.pojo.Vo;
import com.zpq.service.UserCommentService;
import com.zpq.service.UserCommentServiceImpl;

@WebServlet("/SearchCommentServlet")
public class SearchCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // 依赖业务层接口
    private UserCommentService userCommentService = new UserCommentServiceImpl();

    public SearchCommentServlet() {
        super();
    }

    // 工具方法：获取请求参数，为空时返回默认值
    private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Vo vo = new Vo();
        try {
            // 获取分页参数
            int page = Integer.parseInt(request.getParameter("page"));
            int limit = Integer.parseInt(request.getParameter("limit"));

            // 获取搜索条件参数
            long articleId = Long.parseLong(getParamValue(request, "articleId", "-1"));
            String topic = getParamValue(request, "topic", null);
            String name = getParamValue(request, "name", null);

            // 封装搜索条件到实体类
            Usercomment userComment = new Usercomment();
            userComment.setArticleId(articleId);
            userComment.setTopic(topic);
            userComment.setName(name);

            // 调用业务层执行搜索（不直接调用DAO）
            vo = userCommentService.searchComments(userComment, page, limit);

        } catch (SQLException e) {
            e.printStackTrace();
            vo.setCode(1);
            vo.setMsg("服务器异常");
            vo.setCount(0);
            vo.setData(null);
        } catch (NumberFormatException e) {
            // 处理参数格式错误
            vo.setCode(5);
            vo.setMsg("参数格式错误（页码/文章ID必须为数字）");
            vo.setCount(0);
            vo.setData(null);
        }

        // 响应JSON结果
        response.getWriter().write(JSONObject.toJSONString(vo));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}