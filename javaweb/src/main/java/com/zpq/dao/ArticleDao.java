package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;

import com.zpq.pojo.Article;
//文章接口
public interface ArticleDao {
	//所有用户文章
	public List<Object> selectArticle(int page,int limit) throws SQLException;
	public int countArticle() throws SQLException;
	public List<Object> searchArticle(Article article, int page, int limit) throws SQLException;
	//当前用户文章
	public List<Object> selectMyArticle(String userId,int page,int limit) throws SQLException;
	public int countMyArticle(String userId) throws SQLException;
	public List<Object> searchMyArticle(Article article, int page, int limit) throws SQLException;
}
