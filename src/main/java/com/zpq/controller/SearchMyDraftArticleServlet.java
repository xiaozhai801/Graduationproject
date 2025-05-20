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
import com.zpq.dao.ArticleDao;
import com.zpq.dao.ArticleDaoImpl;
import com.zpq.pojo.Draft;
import com.zpq.pojo.Vo;

/**
 * Servlet implementation class SearchMyDraftArticleServlet
 */
@WebServlet("/SearchMyDraftArticleServlet")
public class SearchMyDraftArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchMyDraftArticleServlet() {
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
		// 获取请求中的所有 Cookie
		Cookie[] cookies = request.getCookies();
		String name = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("username".equals(cookie.getName())) {
					name = cookie.getValue();
				}
			}
		}

		// 解析分页参数page
		int page = Integer.parseInt(request.getParameter("page"));
		// 解析分页参数limit
		int limit = Integer.parseInt(request.getParameter("limit"));

		// 获取页面元素值，使用封装方法获取参数值并转换为字符串
		String draftIdStr = getParamValue(request, "draftId", "-1");
		String topic = getParamValue(request, "topic", null);
		String model = getParamValue(request, "model", null);

		// 将titleId的字符串值转换为整数
		int draftId = Integer.parseInt(draftIdStr);

		// 创建Draft对象并设置属性
		Draft draft=new Draft();
		draft.setDraftId(draftId);
		draft.setTopic(topic);
		draft.setModel(model);
		draft.setUserId(name);

		// 创建ArticleDao实例
		ArticleDao articleDao = new ArticleDaoImpl();
		try {
			// 调用searchArticle方法获取文章列表
			List<Object> draftList = articleDao.searchMyDraftArticle(draft, page, limit);
			// 设置网页格式和编码
			response.setContentType("text/html;charset=UTF-8");
			// 创建Vo对象并设置相关属性
			Vo vo = new Vo();
			vo.setCode(0);
			vo.setMsg("success");
			vo.setCount(articleDao.countMyDraftArticle(name));
			vo.setData(draftList);
			// 将Vo对象转换为JSON字符串并返回给客户端
			response.getWriter().write(JSONObject.toJSON(vo).toString());
		} catch (SQLException e) {
			// 捕获SQLException并打印堆栈跟踪信息
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
