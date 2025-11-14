package com.zpq.controller;
// 显示 文章信息,用户互动信息,用户评论信息 1
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Vo;
import com.zpq.service.ArticleService;
import com.zpq.service.ArticleServiceImpl;

/**
 * Servlet implementation class ArticleDetailServlet
 */
@WebServlet("/ArticleDetailServlet")
public class ArticleDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ArticleService articleService = new ArticleServiceImpl();

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArticleDetailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8"); // 统一JSON响应

        // 获取请求参数（文章ID）
        long articleId = Long.parseLong(request.getParameter("articleId"));

        String userName = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userName".equals(cookie.getName())) {
                    userName = cookie.getValue();
                    break;
                }
            }
        }

        // 调用Service层获取文章内容
        Vo vo;
        try {
            vo = articleService.showArticleDetail(articleId,userName);
        } catch (Exception e) {
            e.printStackTrace();
            // 系统异常处理
            vo = new Vo();
            vo.setCode(500);
            vo.setMsg("系统错误：" + e.getMessage());
            vo.setCount(0);
            vo.setData(new ArrayList<>());
        }

        // 响应JSON数据
        response.getWriter().write(JSONObject.toJSONString(vo));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
