package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.zpq.pojo.Useractions;

public interface UseractionsDao {
	// 查询用户行为信息,actionId:Useractions
	public Map<String, Useractions> searchUseractions(String element1, String value1,String element2, String value2) throws SQLException;

	// 查询用户行为信息
	public List<Object> searchUseractions(Long articleId,String userName) throws SQLException;
	
	// 更新用户互动信息
	public int updateUserActions(String userName,long articleId, String like, String favorite) throws SQLException;
	

	// 查询当前用户点赞文章
	public List<Object> SelectMyLike(String userName) throws SQLException;

	// 当前用户点赞文章数量
	public int CountMyLike(String userName) throws SQLException;

	// 查询当前用户收藏文章
	public List<Object> SelectMyFavorite(String userName) throws SQLException;

	// 当前用户收藏文章数量
	public int CountMyFavorite(String userName) throws SQLException;
}
