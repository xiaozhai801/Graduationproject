package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.dao.ArticleDao;
import com.zpq.dao.ArticleDaoImpl;
import com.zpq.dao.SearchElementDao;
import com.zpq.dao.SearchElementDaoImpl;
import com.zpq.pojo.Article;
import com.zpq.pojo.Vo;

/**
 * Servlet implementation class ShowArticleServlet
 */
@WebServlet("/ShowArticleServlet")
public class ShowArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowArticleServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		// 获取页面传入标题ID
		int titleId = Integer.parseInt(request.getParameter("titleId"));

		SearchElementDao searchElement=new SearchElementDaoImpl();
		ArticleDao articleDao=new ArticleDaoImpl();
		Map<Integer, Article> articleInfo;
		try {
			// 获取当前登录用户所选择的文章信息
			articleInfo=searchElement.searchArticleInfo("titleId", titleId+"");
			List<Object> resultList=new ArrayList<>(articleInfo.size());
			resultList.add(articleInfo.get(titleId));
			
			Vo vo = new Vo();
			vo.setCode(0);
			vo.setMsg("success");
			vo.setCount(articleDao.countArticle());
			vo.setData(resultList);
			response.getWriter().write(JSONObject.toJSON(vo).toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
