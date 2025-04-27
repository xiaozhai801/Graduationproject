package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zpq.dao.ArticleDao;
import com.zpq.dao.ArticleDaoImpl;

/**
 * Servlet implementation class DeleteArticeServlet
 */
@WebServlet("/DeleteArticleServlet")
public class DeleteArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteArticleServlet() {
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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		// 获取页面ID
		String draftIdStr = getParamValue(request, "draftId", "-1");
		String titleIdStr = getParamValue(request, "titleId", "-1");

		// 将字符串值转换为整数
		int draftId = Integer.parseInt(draftIdStr);
		int titleId = Integer.parseInt(titleIdStr);

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

		// 根据角色分类不同任务
		ArticleDao articleDao = new ArticleDaoImpl();
		if (role.equals("user")) {
			if (draftId != -1) {
				try {
					// 用户删除草稿操作
					if (articleDao.deleteDraftArticle(draftId, name) == 1) {
						response.getWriter().write("success");
					} else {
						response.getWriter().write("fail");
					}
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (titleId != -1) {
				// 删用户除文章操作
			}
		} else {
			// 管理员操作
			try {
				if (articleDao.deleteArticle(titleId) == 1) {
					response.getWriter().write("success");
				} else {
					response.getWriter().write("fail");
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
