package com.zpq.controller;
// 删除评论 1
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.UserCommentService;
import com.zpq.service.UserCommentServiceImpl;

@WebServlet("/DeleteCommentServlet")
public class DeleteCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // 依赖业务层接口
    private UserCommentService userCommentService = new UserCommentServiceImpl();

    public DeleteCommentServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Vo vo = new Vo();
        try {
            // 获取评论ID参数
            long commentId = Long.parseLong(request.getParameter("commentId"));

            // 调用业务层执行删除（不直接调用DAO）
            vo = userCommentService.deleteComment(commentId);

        } catch (SQLException e) {
            e.printStackTrace();
            vo.setCode(1);
            vo.setMsg("服务器异常");
            vo.setCount(0);
            vo.setData(null);
        } catch (NumberFormatException e) {
            // 处理评论ID格式错误
            vo.setCode(5);
            vo.setMsg("评论ID格式错误（必须为数字）");
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