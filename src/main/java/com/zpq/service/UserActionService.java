package com.zpq.service;

import java.sql.SQLException;

import com.zpq.pojo.Vo;

public interface UserActionService {
    // 更新用户互动行为
    Vo updateUserActions(String userName, long articleId, String like, String favorite) throws SQLException;
    
    // 查询我的点赞
    Vo selectMyLike(String userName) throws SQLException;

    // 查询我的收藏
    Vo selectMyFavorite(String userName) throws SQLException;

}
