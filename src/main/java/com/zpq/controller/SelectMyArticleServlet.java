package com.zpq.controller;
// 显示我的文章 1
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Articleinfo;
import com.zpq.pojo.Vo;
import com.zpq.service.ArticleService;
import com.zpq.service.ArticleServiceImpl;
import com.zpq.service.UserInfoService;
import com.zpq.service.UserInfoServiceImpl;

@WebServlet("/SelectMyArticleServlet")
public class SelectMyArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 依赖文章服务和用户服务
	private ArticleService articleService = new ArticleServiceImpl();
	private UserInfoService userInfoService = new UserInfoServiceImpl();

	public SelectMyArticleServlet() {
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

		// 从Cookie获取userName
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

		// 通过userInfoService获取name（作者名称）
		String name = null;
		try {
			if (userName != null && !userName.isEmpty()) {
				name = userInfoService.getWebName(userName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Vo vo = new Vo();
		try {
			// 校验登录状态和name有效性
			if (userName == null || userName.isEmpty()) {
				vo.setCode(2);
				vo.setMsg("未登录，无法查看我的文章");
				vo.setCount(0);
				vo.setData(null);
				response.getWriter().write(JSONObject.toJSONString(vo));
				return;
			}
			if (name == null || name.isEmpty()) {
				vo.setCode(6);
				vo.setMsg("获取作者信息失败");
				vo.setCount(0);
				vo.setData(null);
				response.getWriter().write(JSONObject.toJSONString(vo));
				return;
			}

			int page = Integer.parseInt(request.getParameter("page"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			String myarticle = getParamValue(request, "myarticle", null);
			Articleinfo articleinfo=new Articleinfo();
			articleinfo.setName(name);
			articleinfo.setRelease(-1);
			if (myarticle.equals("true")) {
				vo = articleService.selectMyArticles(articleinfo, page, limit);
			}else if (myarticle.equals("false")) {
				vo = articleService.selectMyDraftArticles(articleinfo, page, limit);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			vo.setCode(1);
			vo.setMsg("服务器异常");
			vo.setCount(0);
			vo.setData(null);
		} catch (NumberFormatException e) {
			vo.setCode(5);
			vo.setMsg("分页参数格式错误（必须为数字）");
			vo.setCount(0);
			vo.setData(null);
		}

		// 响应结果
		response.getWriter().write(JSONObject.toJSONString(vo));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}