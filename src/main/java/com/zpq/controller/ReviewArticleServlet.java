package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zpq.dao.ArticleDao;
import com.zpq.dao.ArticleDaoImpl;

//审核通过和不通过
/**
 * Servlet implementation class ReviewArticleServlet
 */
@WebServlet("/ReviewArticleServlet")
public class ReviewArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewArticleServlet() {
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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		// 获取页面元素值
		int titleId = Integer.parseInt(request.getParameter("titleId"));
		int passCode = Integer.parseInt(request.getParameter("passCode"));

		ArticleDao articleDao = new ArticleDaoImpl();
		try {
			if (passCode == 1) { // 点击审核通过
				if (articleDao.ReviewPassArticle(titleId) == 1 && articleDao.UploadTitleId(titleId) == 1) { // 审核通过
					response.getWriter().write("success");
				} else {
					response.getWriter().write("fail");
				}
			} else if (passCode == 2) { // 点击审核未通过
				if (articleDao.ReviewNotPassArticle(titleId) == 1) {
					response.getWriter().write("success");
				} else {
					response.getWriter().write("fail");
				}
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
