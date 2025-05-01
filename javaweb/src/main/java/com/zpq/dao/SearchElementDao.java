package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.zpq.pojo.Article;
import com.zpq.pojo.Draft;
import com.zpq.pojo.Model;
import com.zpq.pojo.User;
import com.zpq.pojo.UserAction;

public interface SearchElementDao {
	// 查询用户信息,userId:User
	public Map<String, User> searchUserInfo(String element, String value) throws SQLException;

	// 查询文章信息,titleId:Article
	public Map<Integer, Article> searchArticleInfo(String element, String value) throws SQLException;

	// 查询型号信息,typeId:Model
	public Map<Integer, Model> searchModelInfo(String element, String value) throws SQLException;

	// 查询文章草稿信息,draftId:Draft
	public Map<Integer, Draft> searchDraftInfo(Object element, Object value) throws SQLException;
	
	//当前用户与文章互动情况
	public Map<Long, UserAction> searchUserActionInfo(String userId,int titleId) throws SQLException;
	
	// 显示文章所有评论
	public List<Object> searchUserCommentInfo(int titleId) throws SQLException;
}
