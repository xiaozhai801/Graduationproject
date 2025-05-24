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
import com.zpq.dao.ArticleDao;
import com.zpq.dao.ArticleDaoImpl;
import com.zpq.pojo.Article;
import com.zpq.pojo.Vo;

//搜索文章
/**
 * Servlet implementation class SearchArticleServlet
 */
@WebServlet("/SearchArticleServlet")
public class SearchArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchArticleServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		return (value != null && !value.isEmpty()) ? value : defaultValue;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 解析分页参数page
		int page = Integer.parseInt(request.getParameter("page"));
		// 解析分页参数limit
		int limit = Integer.parseInt(request.getParameter("limit"));

		// 获取页面元素值，使用封装方法获取参数值并转换为字符串
		String titleIdStr = getParamValue(request, "titleId", "-1");
		String topic = getParamValue(request, "topic", null);
		String name = getParamValue(request, "name", null);
		String model = getParamValue(request, "model", null);
		String releaseStr = getParamValue(request, "release", "-1");

		// 将字符串值转换为整数
		int titleId = Integer.parseInt(titleIdStr);
		int release = Integer.parseInt(releaseStr);

		// 创建Article对象并设置属性
		Article article = new Article();
		article.setTitleId(titleId);
		article.setTopic(topic);
		article.setName(name);
		if (model != null && model.equals("全部")) {
			article.setModel(null);
		} else {
			article.setModel(model);
		}
		article.setRelease(release);

		// 创建ArticleDao实例
		ArticleDao articleDao = new ArticleDaoImpl();
		try {
			// 调用searchArticle方法获取文章列表
			List<Object> articleList = articleDao.searchArticle(article, page, limit);
			// 设置网页格式和编码
			response.setContentType("text/html;charset=UTF-8");
			// 创建Vo对象并设置相关属性
			Vo vo = new Vo();
			vo.setCode(0);
			vo.setMsg("success");
			vo.setCount(articleDao.countArticle(article.getModel()));
			vo.setData(articleList);
			// 将Vo对象转换为JSON字符串并返回给客户端
			response.getWriter().write(JSONObject.toJSON(vo).toString());
		} catch (SQLException e) {
			// 捕获SQLException并打印堆栈跟踪信息
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}