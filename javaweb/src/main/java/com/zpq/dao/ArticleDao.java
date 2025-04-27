package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;

import com.zpq.pojo.Article;
import com.zpq.pojo.Draft;

//文章接口
public interface ArticleDao {
	// 所有用户文章列表
	public List<Object> selectArticle(int page, int limit) throws SQLException;

	// 所有文章数量
	public int countArticle() throws SQLException;

	// 搜索文章列表
	public List<Object> searchArticle(Article article, int page, int limit) throws SQLException;

	// 我的文章列表
	public List<Object> selectMyArticle(String userId, int page, int limit) throws SQLException;

	// 我的文章数量
	public int countMyArticle(String userId) throws SQLException;

	// 搜索我的文章列表
	public List<Object> searchMyArticle(Article article, int page, int limit) throws SQLException;

	// 默认分类查询列表
	public List<Object> selectArticle(int page, int limit, String model) throws SQLException;

	// 上传文章草稿
	public int uploadArticle(Draft draft) throws SQLException;

	// 我的文章草稿列表
	public List<Object> selectMyDraftArticle(String userId, int page, int limit) throws SQLException;

	// 我的文章草稿数量
	public int countMyDraftArticle(String userId) throws SQLException;

	// 搜索我的文章草稿列表
	public List<Object> searchMyDraftArticle(Draft draft, int page, int limit) throws SQLException;

	// 上传文章草稿审核
	public int UploadArticleReview(int draftId, String userId) throws SQLException;

	// 获取我的草稿数量
	public int countDraft(String element, String value) throws SQLException;

	// 删除我的文章
	public int deleteArticle(int titleId, String userId) throws SQLException;
	
	// 删除我的文章草稿
	public int deleteDraftArticle(int draftId, String userId) throws SQLException;

	// 管理员删除文章
	public int deleteArticle(int titleId) throws SQLException;
	
	//管理员审核不通过文章
	public int ReviewNotPassArticle(int titleId) throws SQLException;
	
	//管理员审核通过文章
	public int ReviewPassArticle(int titleId) throws SQLException;
	
	//上传文章ID到文章互动量表
	public int UploadTitleId(int titleId) throws SQLException;
}
