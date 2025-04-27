package com.zpq.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.dao.ArticleDao;
import com.zpq.dao.ArticleDaoImpl;
import com.zpq.dao.SearchElementDao;
import com.zpq.dao.SearchElementDaoImpl;
import com.zpq.pojo.Draft;
import com.zpq.pojo.Vo;
//显示当前文章内容
/**
 * Servlet implementation class ShowArticleServlet
 */
@WebServlet("/ShowDraftArticleServlet")
public class ShowDraftArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowDraftArticleServlet() {
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
		doPost(request, response);
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
		//获取页面传入草稿ID
		int draftId=Integer.parseInt(request.getParameter("draftId"));
		
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

		SearchElementDao searchElement = new SearchElementDaoImpl();
		ArticleDao articleDao=new ArticleDaoImpl();
		Map<Integer, Draft> draftInfo;
		try {
			// 获取当前登录用户所选择的草稿信息
			draftInfo = searchElement.searchDraftInfo("draftId", draftId);
            List<Object> resultList=new ArrayList<>(draftInfo.size());
            resultList.add(draftInfo.get(draftId));
            
            Vo vo = new Vo();
            vo.setCode(0);
            vo.setMsg("success");
            vo.setCount(articleDao.countDraft("userId", name));
            vo.setData(resultList);
            response.getWriter().write(JSONObject.toJSON(vo).toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
