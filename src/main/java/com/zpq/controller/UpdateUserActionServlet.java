package com.zpq.controller;
// 更新用户行为 1
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject; // 引入FastJSON
import com.zpq.pojo.Vo;
import com.zpq.service.UserActionService;
import com.zpq.service.UserActionServiceImpl;

@WebServlet("/UpdateUserActionServlet")
public class UpdateUserActionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserActionService userActionService=new UserActionServiceImpl();

    public UpdateUserActionServlet() {
        super();
    }

    private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应格式为JSON（避免中文乱码）
        response.setContentType("application/json;charset=UTF-8");
        
        // 1. 从Cookie获取用户名
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

        // 2. 初始化返回对象Vo
        Vo vo = new Vo();
        try {
            // 校验用户登录状态
            if (userName == null || userName.isEmpty()) {
                vo.setCode(2); // 2表示未登录
                vo.setMsg("未登录");
                vo.setCount(0);
                vo.setData(null);
                response.getWriter().write(JSONObject.toJSONString(vo));
                return;
            }

            // 3. 获取请求参数
            long articleId = Long.parseLong(getParamValue(request, "articleId", "-1"));
            String like = getParamValue(request, "like", null);
            String favorite = getParamValue(request, "favorite", null);

            // 4. 调用业务层获取处理结果（返回Vo对象）
            vo = userActionService.updateUserActions(userName, articleId, like, favorite);

        } catch (SQLException e) {
            e.printStackTrace();
            vo.setCode(1); // 1表示服务器异常
            vo.setMsg("服务器异常");
            vo.setCount(0);
            vo.setData(null);
        } catch (NumberFormatException e) {
            // 处理articleId转换异常（参数格式错误）
            vo.setCode(5); // 5表示参数格式错误
            vo.setMsg("文章ID格式错误");
            vo.setCount(0);
            vo.setData(null);
        }

        // 5. 响应JSON数据（使用FastJSON转换）
        response.getWriter().write(JSONObject.toJSONString(vo));
    }
}