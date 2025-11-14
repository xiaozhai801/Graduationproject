package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.zpq.pojo.Usercomment;

public interface UsercommentDao {
	// 查询用户评论信息,commentId:Usercomment
	public Map<String, Usercomment> searchUsercomment(String element1, String value1,String element2, String value2) throws SQLException;
	
	// 查询用户评论
	public List<Object> searchUsercomment(Long articleId) throws SQLException;
	
	public List<Object> searchUsercomment(Long articleId,String userName) throws SQLException;
	
	// 所有评论信息,带分页
	public List<Object> SelectComment(int page, int limit) throws SQLException;
	
	// 搜索评论信息,带分页
	public List<Object> SearchComment(Usercomment usercomment, int page, int limit) throws SQLException;
	
	// 搜索条件后所有评论数量
	public int CountComment(Usercomment usercomment) throws SQLException;

	// 所有评论数量
	public int CountComment() throws SQLException;
	
	// 用户提交评论
	public int SubmitComment(String userName, long articleId, String comment) throws SQLException;
	
	// 管理员删除评论
	public int deleteComment(Long commentId) throws SQLException;
	
	// 当前用户评论信息
	public List<Object> SelectMyComment(String userName) throws SQLException;

	// 当前用户评论数量
	public int CountMyComment(String userName) throws SQLException;
}
