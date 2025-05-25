package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.dao.UserDao;
import com.zpq.dao.UserDaoImpl;
import com.zpq.pojo.UserComment;
import com.zpq.pojo.Vo;

/**
 * Servlet implementation class SearchCommentServlet
 */
@WebServlet("/SearchCommentServlet")
public class SearchCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchCommentServlet() {
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
		// 解析分页参数page
		int page = Integer.parseInt(request.getParameter("page"));
		// 解析分页参数limit
		int limit = Integer.parseInt(request.getParameter("limit"));

		// 获取页面元素值，使用封装方法获取参数值并转换为字符串
		String titleIdStr = getParamValue(request, "titleId", "-1");
		String topic = getParamValue(request, "topic", null);
		String name = getParamValue(request, "name", null);
		
		// 将字符串值转换为整数
		int titleId = Integer.parseInt(titleIdStr);
		
		// 创建Article对象并设置属性
		UserComment userComment = new UserComment();
		userComment.setTitleId(titleId);
		userComment.setTopic(topic);
		userComment.setName(name);
		
		// 创建ArticleDao实例
		UserDao userDao = new UserDaoImpl();
		try {
			List<Object> userCommentList = userDao.SearchComment(userComment, page, limit);
			response.setContentType("text/html;charset=UTF-8");
			Vo vo = new Vo();
			vo.setCode(0);
			vo.setMsg("success");
			vo.setCount(userDao.CountComment(userComment));
			vo.setData(userCommentList);
			response.getWriter().write(JSONObject.toJSON(vo).toString());
		} catch (SQLException e) {
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
