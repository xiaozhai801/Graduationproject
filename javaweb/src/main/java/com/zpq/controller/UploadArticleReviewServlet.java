package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zpq.dao.ArticleDao;
import com.zpq.dao.ArticleDaoImpl;
import com.zpq.dao.SearchElementDao;
import com.zpq.dao.SearchElementDaoImpl;
import com.zpq.pojo.Draft;
import com.zpq.pojo.Model;
import com.zpq.pojo.User;

/**
 * Servlet implementation class UploadReviewServlet
 */
@WebServlet("/UploadArticleReviewServlet")
public class UploadArticleReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadArticleReviewServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		return (value != null && !value.isEmpty()) ? value : defaultValue;
	}

	private String getDraftId() {
		// 获取当前日期和时间
		LocalDateTime now = LocalDateTime.now();
		// 定义日期时间格式
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddss");
		// 得到草稿ID
		String DraftId = now.format(formatter);
		return DraftId;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
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

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		// 获取页面传入草稿ID
		String draftIdStr = getParamValue(request, "draftId", getDraftId());
		// 转化为整型
		int draftId = Integer.parseInt(draftIdStr);
		// 当前所在阶段
		int status = Integer.parseInt(getParamValue(request, "status", "2"));

		ArticleDao articleDao = new ArticleDaoImpl();
		Draft draft = new Draft();
		try {
			// 文章上传阶段直接发布
			if (status == 1) {
				// 获取标题
				String topic = getParamValue(request, "title", null);
				// 获取html原始格式
				String contentHtml = getParamValue(request, "contentHtml", "");
				contentHtml = contentHtml.replace("../../", "/javaweb/");

				// 获取文本格式
				String contentText = getParamValue(request, "contentText", "");
				String typeId = getParamValue(request, "typeId", "-1");
				// 定义搜索信息工具类
				SearchElementDao searchElement = new SearchElementDaoImpl();
				Map<String, User> userInfo;
				Map<Integer, Model> modelInfo;

				userInfo = searchElement.searchUserInfo("userId", name);
				modelInfo = searchElement.searchModelInfo("typeId", typeId);

				// 设置值
				draft.setDraftId(draftId);
				draft.setTopic(topic);
				draft.setUserId(name);
				draft.setName(userInfo.get(name).getName());
				draft.setModel(modelInfo.get(Integer.parseInt(typeId)).getTypeName());
				draft.setTypeId(Integer.parseInt(typeId));
				draft.setData_html(contentHtml);
				draft.setData_text(contentText);
				articleDao.uploadArticle(draft);
			}
			// 发布给管理员审核
			if (articleDao.UploadArticleReview(draftId, name) == 1) {
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
