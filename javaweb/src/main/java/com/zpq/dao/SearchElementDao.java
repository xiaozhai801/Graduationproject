package com.zpq.dao;

import java.sql.SQLException;
import java.util.Map;

import com.zpq.pojo.Article;
import com.zpq.pojo.Draft;
import com.zpq.pojo.Model;
import com.zpq.pojo.User;

public interface SearchElementDao {
	// 查询用户信息
	public User searchUserInfo(String element, String value) throws SQLException;

	// 查询文章信息
	public Article searchArticleInfo(String element, String value) throws SQLException;

	// 查询型号信息
	public Model searchModelInfo(String element, String value) throws SQLException;

	// 查询文章草稿信息
	public Map<Integer, Draft> searchDraftInfo(Object element, Object value) throws SQLException;

	// 获取作者草稿数量
	public int countDraft(String element, String value) throws SQLException;
}
