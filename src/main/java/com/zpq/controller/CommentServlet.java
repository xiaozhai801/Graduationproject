package com.zpq.controller;
// 查询或提交评论 1
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.UserCommentService;
import com.zpq.service.UserCommentServiceImpl;

@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserCommentService userCommentService=new UserCommentServiceImpl();

    public CommentServlet() {
        super();
    }

    /**
     * 处理评论分页查询
     */
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

            vo = userCommentService.getComments(page, limit);

        } catch (SQLException e) {
            e.printStackTrace();
            vo.setCode(1);
            vo.setMsg("服务器异常");
            vo.setCount(0);
            vo.setData(null);
        } catch (NumberFormatException e) {
            // 处理分页参数格式错误
            vo.setCode(5);
            vo.setMsg("分页参数格式错误");
            vo.setCount(0);
            vo.setData(null);
        }

        // 响应JSON结果
        response.getWriter().write(JSONObject.toJSONString(vo));
    }

    /**
     * 处理评论提交
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 从Cookie获取用户名
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

        Vo vo = new Vo();
        try {
            // 校验用户登录状态
            if (userName == null || userName.isEmpty()) {
                vo.setCode(2);
                vo.setMsg("未登录，无法提交评论");
                vo.setCount(0);
                vo.setData(null);
                response.getWriter().write(JSONObject.toJSONString(vo));
                return;
            }

            // 获取请求参数
            long articleId = Long.parseLong(request.getParameter("articleId"));
            String comment = request.getParameter("comment");

            vo = userCommentService.submitComment(userName, articleId, comment);

        } catch (SQLException e) {
            e.printStackTrace();
            vo.setCode(1);
            vo.setMsg("服务器异常");
            vo.setCount(0);
            vo.setData(null);
        } catch (NumberFormatException e) {
            // 处理文章ID格式错误
            vo.setCode(5);
            vo.setMsg("文章ID格式错误");
            vo.setCount(0);
            vo.setData(null);
        }

        // 响应JSON结果
        response.getWriter().write(JSONObject.toJSONString(vo));
    }
}