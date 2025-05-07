package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.dao.UserDao;
import com.zpq.dao.UserDaoImpl;
import com.zpq.pojo.Vo;

/**
 * Servlet implementation class SelectMyFavoriteServlet
 */
@WebServlet("/SelectMyInteractionServlet")
public class SelectMyInteractionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectMyInteractionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		return (value != null && !value.isEmpty()) ? value : defaultValue;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String like = getParamValue(request, "like", "false");
		String favorite = getParamValue(request, "favorite", "false");
		
		// 获取请求中的用户ID
		Cookie[] cookies = request.getCookies();
		String name = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("username".equals(cookie.getName())) {
					name = cookie.getValue();
				}
			}
		}
		
		UserDao userDao=new UserDaoImpl();
		List<Object> myInteraction = null;
		int myInteractionCount = 0;
		try {
			if (like.equals("true")) {
				myInteraction=userDao.SelectMyLike(name);
				myInteractionCount=userDao.CountMyLike(name);
			}else if (favorite.equals("true")) {
				myInteraction=userDao.SelectMyFavorite(name);
				myInteractionCount=userDao.CountMyFavorite(name);
			}

			// 设置网页格式和编码
			response.setContentType("text/html;charset=UTF-8");
			Vo vo = new Vo();
			vo.setCode(0);
			vo.setMsg("success");
			vo.setCount(myInteractionCount);
			vo.setData(myInteraction);
			response.getWriter().write(JSONObject.toJSON(vo).toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
