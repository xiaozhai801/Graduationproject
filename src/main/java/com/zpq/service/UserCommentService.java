package com.zpq.service;

import java.sql.SQLException;

import com.zpq.pojo.Usercomment;
import com.zpq.pojo.Vo;

public interface UserCommentService {
    // 分页查询评论
    Vo getComments(int page, int limit) throws SQLException;

    // 提交评论
    Vo submitComment(String userName, long articleId, String comment) throws SQLException;
    
    // 搜索评论（带条件分页）
    Vo searchComments(Usercomment userComment, int page, int limit) throws SQLException;
    
    // 管理员删除评论
    Vo deleteComment(long commentId) throws SQLException;
   
    // 查询我的评论
    Vo selectMyComment(String userName) throws SQLException;
}
