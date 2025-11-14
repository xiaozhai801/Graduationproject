package com.zpq.controller;
// 查询个人信息 1
import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Userinfo;
import com.zpq.pojo.Vo;
import com.zpq.service.UserInfoService;
import com.zpq.service.UserInfoServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/PersonalInfoServlet")
public class PersonalInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserInfoService userInfoService = new UserInfoServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");

		String userName = null;
		int role = 0;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("userName".equals(cookie.getName())) {
					userName = cookie.getValue();
				}
				if ("role".equals(cookie.getName())) {
					role = Integer.parseInt(cookie.getValue());
				}
			}
		}

		Vo vo;
		try {
			vo = userInfoService.getPersonalInfo(userName, role);
		} catch (SQLException e) {
			e.printStackTrace();
			vo = new Vo();
			vo.setCode(500);
			vo.setMsg("system error");
			vo.setCount(0);
			vo.setData(new ArrayList<>());
		}

		response.getWriter().write(JSONObject.toJSONString(vo));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		String userName = request.getParameter("userName");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		int age = request.getParameter("age") == null || request.getParameter("age").trim().isEmpty() ? 0: Integer.parseInt(request.getParameter("age"));
		String email = request.getParameter("email");

		Userinfo user = new Userinfo();
		user.setUserName(userName);
		user.setName(name);
		user.setSex(sex);
		user.setAge(age);
		user.setEmail(email);

		Vo vo;
		try {
			vo = userInfoService.updatePersonalInfo(user);
		} catch (SQLException e) {
			e.printStackTrace();
			vo = new Vo();
			vo.setCode(500);
			vo.setMsg("system error");
			vo.setCount(0);
			vo.setData(new ArrayList<>());
		}

		response.getWriter().write(JSONObject.toJSONString(vo));
	}
}