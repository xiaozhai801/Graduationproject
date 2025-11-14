package com.zpq.service;

import com.zpq.pojo.Articleinfo;
import com.zpq.pojo.Vo;
import java.sql.SQLException;

public interface ArticleService {
	// 获取所有待审核和审核通过文章
	Vo getAllAuditArticles(int page, int limit) throws SQLException;

	// 用户删除文章
	boolean deleteUserArticle(int articleId) throws SQLException;

	// 管理员删除文章
	boolean deleteAdminArticle(int articleId) throws SQLException;

	// 保存文章(草稿)
	Vo saveDraftArticle(String userName, String topic, String dataHtml, int typeId) throws SQLException;
	
	// 发布文章审核
    Vo publishArticleForReview(String userName, long articleId, int status, String topic, String dataHtml, int typeId) throws SQLException;
    
    // 工具方法：生成文章ID（复用原逻辑，供无传入ID时使用）
    public String generateArticleId();
    
    // 搜索文章（支持多条件+分页）
    Vo searchArticles(Articleinfo article, int page, int limit) throws SQLException;
    
    // 搜索我的文章（支持多条件+分页）
    Vo searchMyArticles(Articleinfo articleinfo, int page, int limit) throws SQLException;
    
    // 搜索我的文章草稿（支持多条件+分页）
    Vo searchMyDraftArticles(Articleinfo articleinfo, int page, int limit) throws SQLException;
    
    // 显示文章内容（按角色返回不同数据）
    Vo showArticleContent(long articleId) throws SQLException;
    
    // 文章审核（通过/不通过，需管理员权限）
    Vo reviewArticle(long articleId, int passCode, int operatorRole) throws SQLException;
    
    // 显示文章内容,用户互动信息,用户评论信息
    Vo showArticleDetail(long articleId, String userName) throws SQLException;
    
    // 显示我的文章
    Vo selectMyArticles(Articleinfo articleinfo, int page, int limit) throws SQLException;
    
    // 显示我的文章草稿
    Vo selectMyDraftArticles(Articleinfo articleinfo, int page, int limit) throws SQLException;
    
    // 统计各类型文章数量
    Vo countModelArticles() throws SQLException;

}