package com.zpq.controller;
// 搜索文章 1
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zpq.pojo.Articleinfo;
import com.zpq.pojo.Vo;
import com.zpq.service.ArticleService;
import com.zpq.service.ArticleServiceImpl;
import com.zpq.service.UserInfoService;
import com.zpq.service.UserInfoServiceImpl;

@WebServlet("/SearchArticleServlet")
public class SearchArticleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ArticleService articleService = new ArticleServiceImpl();
    private UserInfoService userInfoService=new UserInfoServiceImpl();

    public SearchArticleServlet() {
        super();
    }

    // 获取请求参数，提供默认值
    private String getParamValue(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // 获取分页参数
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));

        // 获取搜索条件参数
        String myarticle = getParamValue(request, "myarticle", "-1"); 
        String articleIdStr = getParamValue(request, "articleId", "-1");
        String topic = getParamValue(request, "topic", null);
        
        String name = getParamValue(request, "name", null);
        String releaseStr = getParamValue(request, "release", "-1");
        if (myarticle.equals("true")) {
    		String userName = null;
    		Cookie[] cookies = request.getCookies();
    		if (cookies != null) {
    			for (Cookie cookie : cookies) {
    				if ("userName".equals(cookie.getName())) {
    					userName = cookie.getValue();
    				}
    			}
    		}
        	
        	try {
				name=userInfoService.getWebName(userName);
				releaseStr="4";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        String typeName = getParamValue(request, "typeName", null);

        // 封装搜索条件到Articleinfo对象
        Articleinfo article = new Articleinfo();
        article.setArticleId(Long.parseLong(articleIdStr)); // 文章ID
        article.setTopic(topic); // 标题关键词
        article.setName(name); // 作者名
        // 类型为“全部”时不参与搜索（置空）
        article.setTypeName(("全部".equals(typeName)) ? null : typeName);
        article.setRelease(Integer.parseInt(releaseStr)); // 发布状态


        // 调用Service层执行搜索，获取结果
        Vo vo;
        try {
            vo = articleService.searchArticles(article, page, limit);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}