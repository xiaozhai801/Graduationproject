package com.zpq.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zpq.dao.UseractionsDao;
import com.zpq.dao.UseractionsDaoImpl;
import com.zpq.pojo.Vo;

public class UserActionServiceImpl implements UserActionService {
	private  UseractionsDao useractionsDao=new UseractionsDaoImpl();
	
    @Override
    public Vo updateUserActions(String userName, long articleId, String like, String favorite) throws SQLException {
        Vo vo = new Vo();
        vo.setCount(0);
        vo.setData(new ArrayList<>()); // 保持数据结构统一

        // 业务参数校验
        if (articleId <= 0) {
            vo.setCode(3); // 3表示参数错误
            vo.setMsg("文章ID无效");
            return vo;
        }

        // 调用DAO层方法
        int result = useractionsDao.updateUserActions(userName, articleId, like, favorite);

        // 封装结果
        if (result == 1) {
            vo.setCode(0); // 0表示成功
            vo.setMsg("操作成功");
        } else {
            vo.setCode(4); // 4表示更新失败
            vo.setMsg("操作失败");
        }

        return vo;
    }
    
    @Override
    public Vo selectMyLike(String userName) throws SQLException {
        Vo vo = new Vo();
        vo.setData(new ArrayList<>());

        List<Object> likeList = useractionsDao.SelectMyLike(userName);
        int count = useractionsDao.CountMyLike(userName);

        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(count);
        vo.setData(likeList);
        return vo;
    }

    @Override
    public Vo selectMyFavorite(String userName) throws SQLException {
        Vo vo = new Vo();
        vo.setData(new ArrayList<>());

        List<Object> favoriteList = useractionsDao.SelectMyFavorite(userName);
        int count = useractionsDao.CountMyFavorite(userName);

        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(count);
        vo.setData(favoriteList);
        return vo;
    }
}
