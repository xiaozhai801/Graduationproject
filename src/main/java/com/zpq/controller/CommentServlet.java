package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
//提交评论
/**
 * Servlet implementation class SubmitReviewServlet
 */
@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		// 点击分页后网页提交的?后参数
		int page = Integer.parseInt(request.getParameter("page"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		
		UserDao userDao=new UserDaoImpl();
		try {
//			List<Object> resultList = new ArrayList<>();
//			resultList.add(userDao.SelectComment(page,limit));
			
			List<Object> resultList = userDao.SelectComment(page,limit);

			Vo vo = new Vo();
			vo.setCode(0);
			vo.setMsg("success");
			vo.setCount(userDao.CountComment());
			vo.setData(resultList);
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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		// 获取页面传入标题ID
		int titleId = Integer.parseInt(request.getParameter("titleId"));
		// 获取页面传入评论信息
		String comment=request.getParameter("comment");
		
		// 获取请求中的用户ID
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
		
		// 获得行为ID
		// 获取当前日期和时间
		LocalDateTime now = LocalDateTime.now();
		// 定义日期时间格式
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddss");
		// 得到草稿ID
		int nowtime = Integer.parseInt(now.format(formatter));
		long id=(long) nowtime*10+Integer.parseInt(name);
		
		UserDao userDao=new UserDaoImpl();
		try {
			if (userDao.SubmitComment(id, name, titleId, comment)==1) {
				response.getWriter().write("success");
			}else {
				response.getWriter().write("fail");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
