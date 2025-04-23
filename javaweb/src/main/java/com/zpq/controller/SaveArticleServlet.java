package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zpq.dao.ArticleDao;
import com.zpq.dao.ArticleDaoImpl;
import com.zpq.pojo.Draft;
import com.zpq.pojo.Model;
import com.zpq.pojo.User;
import com.zpq.utils.SearchElement;

/**
 * Servlet implementation class SaveArticleServlet
 */
@WebServlet("/SaveArticleServlet")
public class SaveArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SaveArticleServlet() {
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
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		return (value != null && !value.isEmpty()) ? value : defaultValue;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
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

		// 获取当前日期和时间
		LocalDateTime now = LocalDateTime.now();
		// 定义日期时间格式
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddss");
		// 得到草稿ID
		int DraftId = Integer.parseInt(now.format(formatter));

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		// 获取标题
		String topic = getParamValue(request, "title", null);
		// 获取html原始格式
		String contentHtml = getParamValue(request, "contentHtml", null);
		// 获取文本格式
		String contentText = getParamValue(request, "contentText", null);
		String typeId = getParamValue(request, "typeId", "-1");

		// 定义搜索信息工具类
		SearchElement searchElement = new SearchElement();
		User userInfo;
		Model modelInfo;
		try {
			userInfo = searchElement.searchUserInfo("userId", name);
			modelInfo = searchElement.searchModelInfo("typeId", typeId);

			// 设置值
			Draft draft = new Draft();
			draft.setDraftId(DraftId);
			draft.setTopic(topic);
			draft.setUserId(name);
			draft.setName(userInfo.getName());
			draft.setModel(modelInfo.getTypeName());
			draft.setTypeId(Integer.parseInt(typeId));
			draft.setData_html(contentHtml);
			draft.setData_text(contentText);

			ArticleDao articleDao = new ArticleDaoImpl();
			if (articleDao.uploadArticle(draft)==1) {
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
