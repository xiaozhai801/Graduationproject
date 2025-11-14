package com.zpq.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zpq.dao.UsercommentDao;
import com.zpq.dao.UsercommentDaoImpl;
import com.zpq.pojo.Usercomment;
import com.zpq.pojo.Vo;

public class UserCommentServiceImpl implements UserCommentService {
	private UsercommentDao usercommentDao=new UsercommentDaoImpl();
	
    @Override
    public Vo getComments(int page, int limit) throws SQLException {
        Vo vo = new Vo();
        vo.setData(new ArrayList<>());

        // 业务参数校验
        if (page < 1 || limit < 1) {
            vo.setCode(3);
            vo.setMsg("分页参数无效（页码和条数必须大于0）");
            vo.setCount(0);
            return vo;
        }

        // 调用DAO层查询数据
        List<Object> commentList = usercommentDao.SelectComment(page, limit);
        int totalCount = usercommentDao.CountComment();

        // 封装结果
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(totalCount);
        vo.setData(commentList);
        return vo;
    }

    @Override
    public Vo submitComment(String userName, long articleId, String comment) throws SQLException {
        Vo vo = new Vo();
        vo.setCount(0);
        vo.setData(new ArrayList<>());

        // 业务参数校验
        if (articleId <= 0) {
            vo.setCode(3);
            vo.setMsg("文章ID无效");
            return vo;
        }
        if (comment == null || comment.trim().isEmpty()) {
            vo.setCode(4);
            vo.setMsg("评论内容不能为空");
            return vo;
        }

        // 调用DAO层提交评论
        int result = usercommentDao.SubmitComment(userName, articleId, comment);

        // 封装结果
        if (result == 1) {
            vo.setCode(0);
            vo.setMsg("success");
        } else {
            vo.setCode(5);
            vo.setMsg("fail");
        }
        return vo;
    }
    
    @Override
    public Vo searchComments(Usercomment userComment, int page, int limit) throws SQLException {
        Vo vo = new Vo();
        vo.setData(new ArrayList<>());

        // 1. 业务参数校验
        if (page < 1 || limit < 1) {
            vo.setCode(3);
            vo.setMsg("分页参数无效（页码和条数必须大于0）");
            vo.setCount(0);
            return vo;
        }

        // 2. 调用DAO层执行搜索
        List<Object> commentList = usercommentDao.SearchComment(userComment, page, limit);
        int totalCount = usercommentDao.CountComment(userComment); // 带条件的总条数

        // 3. 封装结果
        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(totalCount);
        vo.setData(commentList);
        return vo;
    }
    
    @Override
    public Vo deleteComment(long commentId) throws SQLException {
        Vo vo = new Vo();
        vo.setCount(0);
        vo.setData(new ArrayList<>());

        // 业务参数校验
        if (commentId <= 0) {
            vo.setCode(3);
            vo.setMsg("评论ID无效（必须大于0）");
            return vo;
        }

        // 调用DAO层执行删除
        int result = usercommentDao.deleteComment(commentId);

        // 封装结果
        if (result == 1) {
            vo.setCode(0);
            vo.setMsg("success");
        } else {
            vo.setCode(4);
            vo.setMsg("fail");
        }
        return vo;
    }
    
    @Override
    public Vo selectMyComment(String userName) throws SQLException {
        Vo vo = new Vo();
        vo.setData(new ArrayList<>());

        List<Object> commentList = usercommentDao.SelectMyComment(userName);
        int count = usercommentDao.CountMyComment(userName);

        vo.setCode(0);
        vo.setMsg("success");
        vo.setCount(count);
        vo.setData(commentList);
        return vo;
    }
}
