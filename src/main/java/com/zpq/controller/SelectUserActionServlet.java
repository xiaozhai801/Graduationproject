package com.zpq.controller;
// 显示用户行为和评论 1
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
import com.zpq.service.UserActionService;
import com.zpq.service.UserActionServiceImpl;
import com.zpq.service.UserCommentService;
import com.zpq.service.UserCommentServiceImpl;

@WebServlet("/SelectUserActionServlet")
public class SelectUserActionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // 分别依赖两个Service
    private UserActionService userActionService = new UserActionServiceImpl();
    private UserCommentService userCommentService = new UserCommentServiceImpl();

    public SelectUserActionServlet() {
        super();
    }

    private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // 获取行为类型参数
        String like = getParamValue(request, "like", "false");
        String favorite = getParamValue(request, "favorite", "false");
        String comment = getParamValue(request, "comment", "false");

        // 获取用户名
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
            // 校验登录状态
            if (userName == null || userName.isEmpty()) {
                vo.setCode(2);
                vo.setMsg("未登录，无法查询");
                vo.setCount(0);
                vo.setData(null);
                response.getWriter().write(JSONObject.toJSONString(vo));
                return;
            }

            // 校验参数合法性（仅能查询一种行为）
            int actionTypeCount = 0;
            if ("true".equals(like)) actionTypeCount++;
            if ("true".equals(favorite)) actionTypeCount++;
            if ("true".equals(comment)) actionTypeCount++;
            if (actionTypeCount != 1) {
                vo.setCode(3);
                vo.setMsg("参数错误：只能查询一种行为（点赞/收藏/评论）");
                response.getWriter().write(JSONObject.toJSONString(vo));
                return;
            }

            if ("true".equals(like)) {
                vo = userActionService.selectMyLike(userName);
            } else if ("true".equals(favorite)) {
                vo = userActionService.selectMyFavorite(userName);
            } else if ("true".equals(comment)) {
                vo = userCommentService.selectMyComment(userName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            vo.setCode(1);
            vo.setMsg("服务器异常");
            vo.setCount(0);
            vo.setData(null);
        }

        response.getWriter().write(JSONObject.toJSONString(vo));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}