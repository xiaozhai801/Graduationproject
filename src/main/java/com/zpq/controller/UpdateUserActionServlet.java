package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zpq.dao.UserDao;
import com.zpq.dao.UserDaoImpl;

/**
 * Servlet implementation class UpdateUserActionServlet
 */
@WebServlet("/UpdateUserActionServlet")
public class UpdateUserActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserActionServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		return (value != null && !value.isEmpty()) ? value : defaultValue;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 获取请求中的所有 Cookie
		Cookie[] cookies = request.getCookies();
		String name = null;
		String role = null;
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("username".equals(cookie.getName())) {
					name = cookie.getValue();
				}
				if ("role".equals(cookie.getName())) {
					role = cookie.getValue();
				}
			}
		}
		if (role.equals("admin")) {
			name="0";
		}

		// 获取网页传值
		String titleIdStr = getParamValue(request, "titleId", "-1");
		String likeStr = getParamValue(request, "like", null);
		String favoriteStr = getParamValue(request, "favorite", null);
		// 设置正确格式
		int titleId = Integer.parseInt(titleIdStr); // 标题ID
		long id = (long) titleId * 10 + Integer.parseInt(name); // 行为ID

		UserDao userDao = new UserDaoImpl();
		try {
			if (likeStr == null) {
				// 处理收藏
				boolean favorite = favoriteStr.equals("true") ? true : false; // 是否收藏
				boolean flag = userDao.Collect(id, name, titleId, favorite) == 1;
				if (flag && favorite) {
					response.getWriter().write("favorite success");
				} else {
					response.getWriter().write("nofavorite success");
				}
			} else if (favoriteStr == null) {
				// 处理点赞
				boolean like = likeStr.equals("true") ? true : false; // 是否点赞
				boolean flag = userDao.Thumbsup(id, name, titleId, like) == 1;
				if (flag && like) {
					response.getWriter().write("like success");
				} else {
					response.getWriter().write("nolike success");
				}
			} else {
				response.getWriter().write("fail");
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
