package com.zpq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpq.pojo.Articleinfo;
import com.zpq.utils.DBUtil;

public class ArticleinfoDaoImpl implements ArticleinfoDao {

	@Override
	public List<Object> selectArticle(int page, int limit) throws SQLException {
		List<Object> articleList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT * FROM v_articleinfo LIMIT ?,?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setInt(1, (page - 1) * limit);
		ps.setInt(2, limit);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Articleinfo articleinfo = new Articleinfo();
			articleinfo.setArticleId(rs.getLong("articleId"));
			articleinfo.setUserId(rs.getInt("userId"));
			articleinfo.setName(rs.getString("name"));
			articleinfo.setTopic(rs.getString("topic"));
			articleinfo.setTypeName(rs.getString("typeName"));
			articleinfo.setUploadTime(rs.getString("uploadTime"));
			articleinfo.setRelease(rs.getInt("release"));
			articleinfo.setViews(rs.getInt("views"));
			articleinfo.setLikes(rs.getInt("likes"));
			articleinfo.setFavorites(rs.getInt("favorites"));
			articleinfo.setDataHtml(rs.getString("dataHtml"));
			articleList.add(articleinfo);
		}
		// 返回查询到的文章对象列表
		return articleList;
	}

	@Override
	public List<Object> selectAuditArticle(int page, int limit) throws SQLException {
		List<Object> articleList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT * FROM v_articleinfo where `release` != 0 LIMIT ?,?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setInt(1, (page - 1) * limit);
		ps.setInt(2, limit);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Articleinfo articleinfo = new Articleinfo();
			articleinfo.setArticleId(rs.getLong("articleId"));
			articleinfo.setUserId(rs.getInt("userId"));
			articleinfo.setName(rs.getString("name"));
			articleinfo.setTopic(rs.getString("topic"));
			articleinfo.setTypeName(rs.getString("typeName"));
			articleinfo.setUploadTime(rs.getString("uploadTime"));
			articleinfo.setRelease(rs.getInt("release"));
			articleinfo.setViews(rs.getInt("views"));
			articleinfo.setLikes(rs.getInt("likes"));
			articleinfo.setFavorites(rs.getInt("favorites"));
			articleinfo.setDataHtml(rs.getString("dataHtml"));
			articleList.add(articleinfo);
		}
		// 返回查询到的文章对象列表
		return articleList;
	}

	// 获取所有文章数量的方法
	@Override
	public int countArticle() throws SQLException {
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT count(*) as sum FROM v_articleinfo";
		Connection conn = dbUtil.getConnection();
		java.sql.Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()) {
			return rs.getInt("sum");
		}
		// 如果没有查询到结果，返回0
		return 0;
	}

	@Override
	public int countAuditArticle() throws SQLException {
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT count(*) as sum FROM v_articleinfo where `release` != 0";
		Connection conn = dbUtil.getConnection();
		java.sql.Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()) {
			return rs.getInt("sum");
		}
		// 如果没有查询到结果，返回0
		return 0;
	}

	@Override
	public int countArticle(Articleinfo articleinfo) throws SQLException {
		int row = 0;
		DBUtil dbUtil = new DBUtil();
		StringBuilder sqlBuilder = new StringBuilder("SELECT count(*) as sum FROM v_articleinfo WHERE 1=1");
		long articleId = articleinfo.getArticleId();
		String topic = articleinfo.getTopic();
		String name = articleinfo.getName();
		String typeName = articleinfo.getTypeName();
		int release = articleinfo.getRelease();

		List<Object> params = new ArrayList<>();

		if (articleId != -1) {
			sqlBuilder.append(" AND CAST(articleId AS CHAR) LIKE ?");
			params.add("%" + articleId + "%");
		}

		if (topic != null) {
			sqlBuilder.append(" AND topic LIKE ?");
			params.add("%" + topic + "%");
		}

		if (name != null) {
			sqlBuilder.append(" AND name LIKE ?");
			params.add("%" + name + "%");
		}

		if (typeName != null) {
			sqlBuilder.append(" AND typeName LIKE ?");
			params.add("%" + typeName + "%");
		}

		if (release != -1) {
			sqlBuilder.append(" AND `release` = ?");
			params.add(release);
		} else {
			sqlBuilder.append(" AND `release` != 0");
		}

		// 将StringBuilder中的内容转换为完整的SQL语句字符串
		String sql = sqlBuilder.toString();
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		for (int i = 0; i < params.size(); i++) {
			ps.setObject(i + 1, params.get(i));
		}

		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			row = rs.getInt("sum");
		}
		return row;
	}

	@Override
	public List<Object> selectMyArticle(String name, int page, int limit) throws SQLException {
		// 用于存储查询到的文章对象的列表
		List<Object> articleList = new ArrayList<>();
		// 创建数据库工具类实例，用于获取数据库连接和操作数据库
		DBUtil dbUtil = new DBUtil();
		// SQL语句，使用LIMIT进行分页查询，?为占位符
		String sql = "SELECT * FROM v_articleinfo where name=? LIMIT ?,?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		// 设置where字句的第一个参数,即查询的用户
		ps.setString(1, name);
		// 设置LIMIT子句的第一个参数，即偏移量，(page - 1) * limit计算出从哪条数据开始查询
		ps.setInt(2, (page - 1) * limit);
		// 设置LIMIT子句的第二个参数，即每页显示的记录数
		ps.setInt(3, limit);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		// 遍历结果集，将每一条记录封装成Article对象并添加到articleList中
		while (rs.next()) {
			Articleinfo articleinfo = new Articleinfo();
			articleinfo.setArticleId(rs.getLong("articleId"));
			articleinfo.setUserId(rs.getInt("userId"));
			articleinfo.setName(rs.getString("name"));
			articleinfo.setTopic(rs.getString("topic"));
			articleinfo.setTypeName(rs.getString("typeName"));
			articleinfo.setUploadTime(rs.getString("uploadTime"));
			articleinfo.setRelease(rs.getInt("release"));
			articleinfo.setViews(rs.getInt("views"));
			articleinfo.setLikes(rs.getInt("likes"));
			articleinfo.setFavorites(rs.getInt("favorites"));
			articleinfo.setDataHtml(rs.getString("dataHtml"));
			articleList.add(articleinfo);
		}
		// 返回查询到的文章对象列表
		return articleList;
	}

	@Override
	public int countMyArticle(Articleinfo articleinfo) throws SQLException {
		int row = 0;
		DBUtil dbUtil = new DBUtil();
		StringBuilder sqlBuilder = new StringBuilder("SELECT count(*) as sum FROM v_articleinfo WHERE 1=1");
		long articleId = articleinfo.getArticleId();
		String topic = articleinfo.getTopic();
		String name = articleinfo.getName();
		String typeName = articleinfo.getTypeName();
		int release = articleinfo.getRelease();
		List<Object> params = new ArrayList<>();

		if (articleId != -1) {
			sqlBuilder.append(" AND CAST(articleId AS CHAR) LIKE ?");
			params.add("%" + articleId + "%");
		}

		if (topic != null) {
			sqlBuilder.append(" AND topic LIKE ?");
			params.add("%" + topic + "%");
		}

		if (name != null) {
			sqlBuilder.append(" AND name LIKE ?");
			params.add("%" + name + "%");
		}

		if (typeName != null) {
			sqlBuilder.append(" AND typeName LIKE ?");
			params.add("%" + typeName + "%");
		}
		
		if (release != -1) {
			sqlBuilder.append(" AND `release` = ?");
			params.add(release);
		}
		
		// 将StringBuilder中的内容转换为完整的SQL语句字符串
		String sql = sqlBuilder.toString();
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		for (int i = 0; i < params.size(); i++) {
			ps.setObject(i + 1, params.get(i));
		}
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			row = rs.getInt("sum");
		}
		return row;
	}

	@Override
	public List<Object> searchMyArticle(Articleinfo articleinfo, int page, int limit) throws SQLException {
		// 用于存储搜索到的文章对象的列表
		List<Object> searchArticleList = new ArrayList<>();
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		// 使用StringBuilder构建SQL查询语句，初始语句为查询所有记录的基础部分，并添加WHERE 1=1方便后续拼接条件
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM v_articleinfo WHERE 1=1");
		long articleId = articleinfo.getArticleId();
		String topic = articleinfo.getTopic();
		String name = articleinfo.getName();
		String typeName = articleinfo.getTypeName();
		int release = articleinfo.getRelease();
		List<Object> params = new ArrayList<>();

		// 如果文章ID不为-1，说明有文章ID的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (articleId != -1) {
			sqlBuilder.append(" AND CAST(articleId AS CHAR) LIKE ?");
			params.add("%" + articleId + "%");
		}
		
		// 如果文章标题不为null，说明有文章标题的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (topic != null) {
			sqlBuilder.append(" AND topic LIKE ?");
			params.add("%" + topic + "%");
		}
		
		// 文章作者ID的查询条件，添加到SQL语句中，并将参数值添加到params列表
		sqlBuilder.append(" AND name = ?");
		params.add(name);
		
		// 如果文章机型不为null，说明有文章机型的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (typeName != null) {
			sqlBuilder.append(" AND typeName LIKE ?");
			params.add("%" + typeName + "%");
		}
		
		if (release != -1) {
			sqlBuilder.append(" AND `release` = ?");
			params.add(release);
		}

		// 添加分页查询的LIMIT子句到SQL语句中，并将分页参数值添加到params列表
		sqlBuilder.append(" LIMIT ?,?");
		params.add((page - 1) * limit);
		params.add(limit);

		// 将StringBuilder中的内容转换为完整的SQL语句字符串
		String sql = sqlBuilder.toString();

		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		// 遍历params列表，为预编译语句的占位符设置对应的参数值
		for (int i = 0; i < params.size(); i++) {
			ps.setObject(i + 1, params.get(i));
		}

		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		// 遍历结果集，将每一条记录封装成Article对象并添加到searchArticleList中
		while (rs.next()) {
			Articleinfo searchArticleInfo = new Articleinfo();
			searchArticleInfo.setArticleId(rs.getLong("articleId"));
			searchArticleInfo.setUserId(rs.getInt("userId"));
			searchArticleInfo.setName(rs.getString("name"));
			searchArticleInfo.setTopic(rs.getString("topic"));
			searchArticleInfo.setTypeName(rs.getString("typeName"));
			searchArticleInfo.setUploadTime(rs.getString("uploadTime"));
			searchArticleInfo.setRelease(rs.getInt("release"));
			searchArticleInfo.setViews(rs.getInt("views"));
			searchArticleInfo.setLikes(rs.getInt("likes"));
			searchArticleInfo.setFavorites(rs.getInt("favorites"));
			searchArticleInfo.setDataHtml(rs.getString("dataHtml"));
			searchArticleList.add(searchArticleInfo);
		}
		// 返回搜索到的文章对象列表
		return searchArticleList;
	}
	
	@Override
	public List<Object> searchMyDraftArticle(Articleinfo articleinfo, int page, int limit) throws SQLException {
		// 用于存储搜索到的文章对象的列表
		List<Object> searchArticleList = new ArrayList<>();
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		// 使用StringBuilder构建SQL查询语句，初始语句为查询所有记录的基础部分，并添加WHERE 1=1方便后续拼接条件
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM v_articleinfo WHERE 1=1");
		long articleId = articleinfo.getArticleId();
		String topic = articleinfo.getTopic();
		String name = articleinfo.getName();
		String typeName = articleinfo.getTypeName();
		List<Object> params = new ArrayList<>();

		// 如果文章ID不为-1，说明有文章ID的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (articleId != -1) {
			sqlBuilder.append(" AND CAST(articleId AS CHAR) LIKE ?");
			params.add("%" + articleId + "%");
		}
		
		// 如果文章标题不为null，说明有文章标题的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (topic != null) {
			sqlBuilder.append(" AND topic LIKE ?");
			params.add("%" + topic + "%");
		}
		
		// 如果文章机型不为null，说明有文章机型的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (typeName != null) {
			sqlBuilder.append(" AND typeName LIKE ?");
			params.add("%" + typeName + "%");
		}
		
		sqlBuilder.append(" AND name = ? AND `release` IN (0,2)");
		params.add(name);
		
		// 添加分页查询的LIMIT子句到SQL语句中，并将分页参数值添加到params列表
		sqlBuilder.append(" LIMIT ?,?");
		params.add((page - 1) * limit);
		params.add(limit);

		// 将StringBuilder中的内容转换为完整的SQL语句字符串
		String sql = sqlBuilder.toString();

		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		// 遍历params列表，为预编译语句的占位符设置对应的参数值
		for (int i = 0; i < params.size(); i++) {
			ps.setObject(i + 1, params.get(i));
		}

		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		// 遍历结果集，将每一条记录封装成Article对象并添加到searchArticleList中
		while (rs.next()) {
			Articleinfo searchArticleInfo = new Articleinfo();
			searchArticleInfo.setArticleId(rs.getLong("articleId"));
			searchArticleInfo.setUserId(rs.getInt("userId"));
			searchArticleInfo.setName(rs.getString("name"));
			searchArticleInfo.setTopic(rs.getString("topic"));
			searchArticleInfo.setTypeName(rs.getString("typeName"));
			searchArticleInfo.setUploadTime(rs.getString("uploadTime"));
			searchArticleInfo.setRelease(rs.getInt("release"));
			searchArticleInfo.setViews(rs.getInt("views"));
			searchArticleInfo.setLikes(rs.getInt("likes"));
			searchArticleInfo.setFavorites(rs.getInt("favorites"));
			searchArticleInfo.setDataHtml(rs.getString("dataHtml"));
			searchArticleList.add(searchArticleInfo);
		}
		// 返回搜索到的文章对象列表
		return searchArticleList;
	}

//	@Override
//	public List<Object> selectArticle(int page, int limit, String model) throws SQLException {
//		// 用于存储查询到的文章对象的列表
//		List<Object> articleList = new ArrayList<>();
//		// 创建数据库工具类实例，用于获取数据库连接和操作数据库
//		DBUtil dbUtil = new DBUtil();
//		// SQL语句，使用LIMIT进行分页查询，?为占位符
//		String sql = "SELECT * FROM v_articleinfo where model=? LIMIT ?,?";
//		// 获取预编译的SQL语句对象
//		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
//		ps.setString(1, model);
//		// 设置LIMIT子句的第一个参数，即偏移量，(page - 1) * limit计算出从哪条数据开始查询
//		ps.setInt(2, (page - 1) * limit);
//		// 设置LIMIT子句的第二个参数，即每页显示的记录数
//		ps.setInt(3, limit);
//		// 执行SQL查询，并获取结果集
//		ResultSet rs = ps.executeQuery();
//		// 遍历结果集，将每一条记录封装成Article对象并添加到articleList中
//		while (rs.next()) {
//			Article article = new Article();
//			article.setTitleId(rs.getInt("titleId"));
//			article.setUserId(rs.getString("userId"));
//			article.setName(rs.getString("name"));
//			article.setTopic(rs.getString("topic"));
//			article.setModel(rs.getString("model"));
//			article.setUploadTime(rs.getString("uploadTime"));
//			article.setRelease(rs.getInt("release"));
//			article.setViews(rs.getInt("views"));
//			article.setLikes(rs.getInt("likes"));
//			article.setFavorites(rs.getInt("favorites"));
//			article.setData_html(rs.getString("data_html"));
//			article.setData_text(rs.getString("data_text"));
//			articleList.add(article);
//		}
//		// 返回查询到的文章对象列表
//		return articleList;
//	}
//
	@Override
	public int uploadArticleInfo(Articleinfo articleinfo) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		PreparedStatement ps = null;
		int rowsAffected = 0;

		// 执行删除操作（如果需要发布）
		if (articleinfo.getRelease() == 1) {
			// SQL删除草稿文章语句
			String deleteSql = "DELETE FROM v_articleinfo WHERE topic = ? and userId= ?";
			ps = dbUtil.getPreparedStatement(deleteSql);
			ps.setString(1, articleinfo.getTopic());
			ps.setLong(2, articleinfo.getUserId());
			rowsAffected = ps.executeUpdate();
			ps.close();
		}

		// 再执行插入操作
		String sql = "INSERT INTO v_articleinfo (articleId,userId,`name`,topic,typeName,`release`,dataHtml) values (?,?,?,?,?,?,?)";
		ps = dbUtil.getPreparedStatement(sql);
		ps.setLong(1, articleinfo.getArticleId());
		ps.setInt(2, articleinfo.getUserId());
		ps.setString(3, articleinfo.getName());
		ps.setString(4, articleinfo.getTopic());
		ps.setString(5, articleinfo.getTypeName());
		ps.setInt(6, articleinfo.getRelease());
		ps.setString(7, articleinfo.getDataHtml());

		// 执行插入操作
		rowsAffected = ps.executeUpdate();

		// 检查是否操作成功
		return rowsAffected > 0 ? 1 : -1;
	}

	@Override
	public List<Object> selectMyDraftArticle(String name, int page, int limit) throws SQLException {
		List<Object> articleList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT * FROM v_articleinfo where name=? and `release` IN (0,2) LIMIT ?,?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, name);
		ps.setInt(2, (page - 1) * limit);
		ps.setInt(3, limit);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Articleinfo info = new Articleinfo();
			info.setArticleId(rs.getLong("articleId"));
			info.setUserId(rs.getInt("userId"));
			info.setName(rs.getString("name"));
			info.setTopic(rs.getString("topic"));
			info.setTypeName(rs.getString("typeName"));
			info.setUploadTime(rs.getString("uploadTime"));
			info.setRelease(rs.getInt("release"));
			info.setViews(rs.getInt("views"));
			info.setLikes(rs.getInt("likes"));
			info.setFavorites(rs.getInt("favorites"));
			info.setDataHtml(rs.getString("dataHtml"));
			articleList.add(info);
		}
		// 返回查询到的文章对象列表
		return articleList;
	}

	@Override
	public int countMyDraftArticle(Articleinfo articleinfo) throws SQLException {
		DBUtil dbUtil = new DBUtil();
		StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) AS sum FROM v_articleinfo WHERE 1=1");
		List<Object> params = new ArrayList<>();

		long articleId = articleinfo.getArticleId();
		String topic = articleinfo.getTopic();
		String typeName = articleinfo.getTypeName();
		String name = articleinfo.getName();

		if (articleId != -1) {
			sqlBuilder.append(" AND CAST(articleId AS CHAR) LIKE ?");
			params.add("%" + articleId + "%");
		}

		if (topic != null) {
			sqlBuilder.append(" AND topic LIKE ?");
			params.add("%" + topic + "%");
		}

		if (typeName != null) {
			sqlBuilder.append(" AND typeName LIKE ?");
			params.add("%" + typeName + "%");
		}

		sqlBuilder.append(" AND name = ? AND `release` IN (0,2)");
		params.add(name);

		String sql = sqlBuilder.toString();

		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		for (int i = 0; i < params.size(); i++) {
			ps.setObject(i + 1, params.get(i));
		}

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getInt("sum");
		}
		return 0;
	}

//	@Override
//	public List<Object> searchMyDraftArticle(Draft draft, int page, int limit) throws SQLException {
//		// 用于存储搜索到的文章对象的列表
//		List<Object> searchDraftArticleList = new ArrayList<>();
//		// 创建数据库工具类实例
//		DBUtil dbUtil = new DBUtil();
//		// 使用StringBuilder构建SQL查询语句，初始语句为查询所有记录的基础部分，并添加WHERE 1=1方便后续拼接条件
//		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM v_articledraft WHERE 1=1");
//		int draftId = draft.getDraftId();
//		String topic = draft.getTopic();
//		String model = draft.getModel();
//		String userId = draft.getUserId();
//		List<Object> params = new ArrayList<>();
//
//		// 如果文章ID不为-1，说明有文章ID的查询条件，添加到SQL语句中，并将参数值添加到params列表
//		if (draftId != -1) {
//			sqlBuilder.append(" AND CAST(draftId AS CHAR) LIKE ?");
//			params.add("%" + draftId + "%");
//		}
//		// 如果文章标题不为null，说明有文章标题的查询条件，添加到SQL语句中，并将参数值添加到params列表
//		if (topic != null) {
//			sqlBuilder.append(" AND topic LIKE ?");
//			params.add("%" + topic + "%");
//		}
//		// 文章作者ID的查询条件，添加到SQL语句中，并将参数值添加到params列表
//		sqlBuilder.append(" AND userId = ?");
//		params.add(userId);
//		// 如果文章机型不为null，说明有文章机型的查询条件，添加到SQL语句中，并将参数值添加到params列表
//		if (model != null) {
//			sqlBuilder.append(" AND model LIKE ?");
//			params.add("%" + model + "%");
//		}
//
//		// 添加分页查询的LIMIT子句到SQL语句中，并将分页参数值添加到params列表
//		sqlBuilder.append(" LIMIT ?,?");
//		params.add((page - 1) * limit);
//		params.add(limit);
//
//		// 将StringBuilder中的内容转换为完整的SQL语句字符串
//		String sql = sqlBuilder.toString();
//
//		// 获取预编译的SQL语句对象
//		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
//		// 遍历params列表，为预编译语句的占位符设置对应的参数值
//		for (int i = 0; i < params.size(); i++) {
//			ps.setObject(i + 1, params.get(i));
//		}
//
//		// 执行SQL查询，并获取结果集
//		ResultSet rs = ps.executeQuery();
//		// 遍历结果集，将每一条记录封装成Article对象并添加到searchArticleList中
//		while (rs.next()) {
//			Draft searchDraft = new Draft();
//			searchDraft.setDraftId(rs.getInt("draftId"));
//			searchDraft.setTopic(rs.getString("topic"));
//			searchDraft.setName(rs.getString("name"));
//			searchDraft.setModel(rs.getString("model"));
//			searchDraft.setData_text(rs.getString("data_text"));
//			searchDraftArticleList.add(searchDraft);
//		}
//		// 返回搜索到的文章对象列表
//		return searchDraftArticleList;
//	}

//	@Override
//	public int countDraft(String element, String value) throws SQLException {
//		// TODO Auto-generated method stub
//		// 创建数据库工具类实例
//		DBUtil dbUtil = new DBUtil();
//		// SQL语句，统计v_articleinfo表中的记录数量，使用count(*)函数，并将结果命名为sum
//		String sql = "SELECT count(*) as sum FROM v_articledraft where " + element + "=?";
//		// 获取预编译的SQL语句对象
//		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
//		ps.setString(1, value);
//		// 执行SQL查询，并获取结果集
//		ResultSet rs = ps.executeQuery();
//		// 遍历结果集，获取统计的文章数量并返回
//		while (rs.next()) {
//			return rs.getInt("sum");
//		}
//		// 如果没有查询到结果，返回0
//		return 0;
//	}

	@Override
	public int deleteArticle(long articleId) throws SQLException {
		// TODO Auto-generated method stub
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		// SQL删除语句
		String sql = "DELETE FROM v_articleinfo WHERE articleId = ?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setLong(1, articleId);
		// 1 删除成功,0 删除失败
		int row = ps.executeUpdate();
		return row;
	}

	@Override
	public int updateArticleReleaseStatus(long articleId, int releaseStatus) throws SQLException {
		// 校验状态合法性（仅允许3=通过、2=不通过）
		if (releaseStatus != 2 && releaseStatus != 3) {
			throw new IllegalArgumentException("无效的审核状态：仅支持2（不通过）或3（通过）");
		}

		DBUtil dbUtil = new DBUtil();
		String sql = "UPDATE v_articleinfo SET `release` = ? WHERE articleId = ?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setInt(1, releaseStatus); // 动态设置状态
		ps.setLong(2, articleId); // 设置文章ID
		int row = ps.executeUpdate(); // 执行更新

		return row;
	}

	@Override
	public int countModelArticle(String typeName) throws SQLException {
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		String sql;
		PreparedStatement ps;
		if (typeName.equals("all")) {
			sql = "SELECT count(*) as sum FROM v_articleinfo where `release` = 3";
			ps = dbUtil.getPreparedStatement(sql);
		} else {
			sql = "SELECT count(*) as sum FROM v_articleinfo where typeName=? and `release` = 3";
			ps = dbUtil.getPreparedStatement(sql);
			ps.setString(1, typeName);
		}

		ResultSet rs = ps.executeQuery();
		int count = 0;
		if (rs.next()) {
			count = rs.getInt("sum");
		}
		return count;
	}
	
	@Override
	public Map<Long, Articleinfo> searchArticleInfo(Object element, Object value) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		Articleinfo article = new Articleinfo();
		Map<Long, Articleinfo> articleInfo = new HashMap<>();
		String sql = "SELECT * FROM v_articleinfo where " + element + "=?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setObject(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			article.setArticleId(rs.getLong("articleId"));
			article.setUserId(rs.getInt("userId"));
			article.setName(rs.getString("name"));
			article.setTopic(rs.getString("topic"));
			article.setTypeName(rs.getString("typeName"));
			article.setUploadTime(rs.getString("uploadTime"));
			article.setRelease(rs.getInt("release"));
			article.setViews(rs.getInt("views"));
			article.setLikes(rs.getInt("likes"));
			article.setFavorites(rs.getInt("favorites"));
			article.setDataHtml(rs.getString("dataHtml"));
			articleInfo.put(article.getArticleId(), article);
		}
		// 返回查询到的文章对象列表
		return articleInfo;
	}

	@Override
	public List<Object> searchArticleInfo(Articleinfo articleinfo, int page, int limit) throws SQLException {
		List<Object> searchArticleList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		// 使用StringBuilder构建SQL查询语句，初始语句为查询所有记录的基础部分，并添加WHERE 1=1方便后续拼接条件
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM v_articleinfo WHERE 1=1");
		long articleId = articleinfo.getArticleId();
		String topic = articleinfo.getTopic();
		String name = articleinfo.getName();
		String typeName = articleinfo.getTypeName();
		int release = articleinfo.getRelease();
		List<Object> params = new ArrayList<>();

		// 如果文章ID不为-1，说明有文章ID的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (articleId != -1) {
			sqlBuilder.append(" AND CAST(articleId AS CHAR) LIKE ?");
			params.add("%" + articleId + "%");
		}
		// 如果文章标题不为null，说明有文章标题的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (topic != null) {
			sqlBuilder.append(" AND topic LIKE ?");
			params.add("%" + topic + "%");
		}
		// 如果文章作者不为null，说明有文章作者的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (name != null) {
			sqlBuilder.append(" AND name LIKE ?");
			params.add("%" + name + "%");
		}
		// 如果文章机型不为null，说明有文章机型的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (typeName != null) {
			sqlBuilder.append(" AND typeName LIKE ?");
			params.add("%" + typeName + "%");
		}
		// 如果文章审核状态不为-1，说明有文章机型的查询条件，添加到SQL语句中，并将参数值添加到params列表
		if (release != -1) {
			sqlBuilder.append(" AND `release` = ?");
			params.add(release);
		}

		// 添加分页查询的LIMIT子句到SQL语句中，并将分页参数值添加到params列表
		sqlBuilder.append(" LIMIT ?,?");
		params.add((page - 1) * limit);
		params.add(limit);

		// 将StringBuilder中的内容转换为完整的SQL语句字符串
		String sql = sqlBuilder.toString();

		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		// 遍历params列表，为预编译语句的占位符设置对应的参数值
		for (int i = 0; i < params.size(); i++) {
			ps.setObject(i + 1, params.get(i));
		}

		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		// 遍历结果集，将每一条记录封装成Article对象并添加到searchArticleList中
		while (rs.next()) {
			Articleinfo searchArticleInfo = new Articleinfo();
			searchArticleInfo.setArticleId(rs.getLong("articleId"));
			searchArticleInfo.setUserId(rs.getInt("userId"));
			searchArticleInfo.setName(rs.getString("name"));
			searchArticleInfo.setTopic(rs.getString("topic"));
			searchArticleInfo.setTypeName(rs.getString("typeName"));
			searchArticleInfo.setUploadTime(rs.getString("uploadTime"));
			searchArticleInfo.setRelease(rs.getInt("release"));
			searchArticleInfo.setViews(rs.getInt("views"));
			searchArticleInfo.setLikes(rs.getInt("likes"));
			searchArticleInfo.setFavorites(rs.getInt("favorites"));
			searchArticleList.add(searchArticleInfo);
		}
		// 返回搜索到的文章对象列表
		return searchArticleList;
	}

	@Override
	public List<Articleinfo> searchArticleInfo(Long articleId) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		Articleinfo article = new Articleinfo();
		List<Articleinfo> articleInfo = new ArrayList<>();
		String sql = "SELECT * FROM v_articleinfo where articleId=?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setLong(1, articleId);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			article.setArticleId(rs.getLong("articleId"));
			article.setUserId(rs.getInt("userId"));
			article.setName(rs.getString("name"));
			article.setTopic(rs.getString("topic"));
			article.setTypeName(rs.getString("typeName"));
			article.setUploadTime(rs.getString("uploadTime"));
			article.setRelease(rs.getInt("release"));
			article.setViews(rs.getInt("views"));
			article.setLikes(rs.getInt("likes"));
			article.setFavorites(rs.getInt("favorites"));
			article.setDataHtml(rs.getString("dataHtml"));
			articleInfo.add(article);
		}
		// 返回查询到的文章对象列表
		return articleInfo;
	}
}