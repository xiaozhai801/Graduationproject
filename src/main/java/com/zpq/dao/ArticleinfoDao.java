package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.zpq.pojo.Articleinfo;

//文章接口
public interface ArticleinfoDao {
	// 所有用户文章列表
	public List<Object> selectArticle(int page, int limit) throws SQLException;
	
	// 所有用户提交审核文章列表
	public List<Object> selectAuditArticle(int page, int limit) throws SQLException;
//
//	// 分机型查询列表
//	public List<Object> selectArticle(int page, int limit, String model) throws SQLException;

	// 所有文章数量
	public int countArticle() throws SQLException;

	// 文章审核页面所有文章
	public int countAuditArticle() throws SQLException;
	
	// 搜索条件所有文章数量
	public int countArticle(Articleinfo articleinfo) throws SQLException;
	
	// 分机型文章数量
	public int countModelArticle(String typeName) throws SQLException;

	// 上传文章草稿
	public int uploadArticleInfo(Articleinfo articleinfo) throws SQLException;
//
//	// 获取我的草稿数量
//	public int countDraft(String element, String value) throws SQLException;

	// 删除文章
	public int deleteArticle(long articleId) throws SQLException;

	// 管理员审核文章通过或不通过
	public int updateArticleReleaseStatus(long articleId, int releaseStatus) throws SQLException;
	
	// 查询文章信息,articleId:Articleinfo
	public Map<Long, Articleinfo> searchArticleInfo(Object element, Object value) throws SQLException;
	
	// 查询文章信息
	public List<Object> searchArticleInfo(Articleinfo articleinfo, int page, int limit) throws SQLException;
	
	// 查询文章信息
	public List<Articleinfo> searchArticleInfo(Long articleId) throws SQLException;
	
	// 我的文章列表
	public List<Object> selectMyArticle(String name, int page, int limit) throws SQLException;

	// 我的文章数量
	public int countMyArticle(Articleinfo articleinfo) throws SQLException;
	
	// 我的文章草稿列表
	public List<Object> selectMyDraftArticle(String name, int page, int limit) throws SQLException;

	// 我的文章草稿数量
	public int countMyDraftArticle(Articleinfo articleinfo) throws SQLException;
	
	// 搜索我的文章列表,带分页
	public List<Object> searchMyArticle(Articleinfo articleinfo, int page, int limit) throws SQLException;

	// 搜索我的文章草稿列表
	public List<Object> searchMyDraftArticle(Articleinfo articleinfo, int page, int limit) throws SQLException;
}
