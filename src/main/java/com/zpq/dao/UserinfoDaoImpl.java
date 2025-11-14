package com.zpq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpq.pojo.Userinfo;
import com.zpq.utils.DBUtil;

public class UserinfoDaoImpl implements UserinfoDao {
	// 连接数据库查找用户名和密码
	@Override
	public Userinfo selectUser(String userName, String password) throws SQLException {
		Userinfo user = new Userinfo();
		DBUtil dbUtil = new DBUtil();
		String sql = "select * from s_userinfo where userName=? and `password`=?";
		// 有?占位符，用PerpaerdStatement
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, userName);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			user.setName(rs.getString("userName"));
			user.setPassword(rs.getString("password"));
			user.setRole(rs.getInt("role"));
			return user;
		}
		return null;
	}
//	
//	@Override
//	public void Updateinformation(long articleId) throws SQLException {
//		// TODO Auto-generated method stub
//		DBUtil dbUtil = new DBUtil();
//		Articleinfo articleinfo = new Articleinfo();
//		String selectSql = "SELECT views,likes,favorties FROM v_articleinfo WHERE articleId =?;";
//		PreparedStatement ps = dbUtil.getPreparedStatement(selectSql);
//		ps.setLong(1, articleId);
//		ResultSet rs = ps.executeQuery();
//		while (rs.next()) {
//			articleinfo.setViews(rs.getInt("views"));
//			articleinfo.setLikes(rs.getInt("likes"));
//			articleinfo.setFavorites(rs.getInt("favorties"));
//		}
//
//		String updateSql = "UPDATE v_articleinfo set views=?,likes=?,favorites=? where titleId=?";
//		ps = dbUtil.getPreparedStatement(updateSql);
//		ps.setInt(1, article.getViews());
//		ps.setInt(2, article.getLikes());
//		ps.setInt(3, article.getFavorites());
//		ps.setInt(4, titleId);
//		ps.executeUpdate();
//	}

	@Override
	public List<Object> SelectAllUserLsit(int page, int limit) throws SQLException {
		// TODO Auto-generated method stub
		List<Object> userList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT * FROM s_userinfo LIMIT ?,?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setInt(1, (page - 1) * limit);
		ps.setInt(2, limit);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Userinfo userinfo = new Userinfo();
			// 查询user信息
			userinfo.setUserName(rs.getString("userName"));
			userinfo.setName(rs.getString("name"));
			userinfo.setSex(rs.getString("sex"));
			userinfo.setAge(rs.getInt("age"));
			userinfo.setEmail(rs.getString("email"));
			userList.add(userinfo);
		}
		return userList;
	}

	@Override
	public int countUser() throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		String sql = "SELECT count(*) as sum FROM s_userinfo";
		Connection conn = dbUtil.getConnection();
		// 没有?占位符，用Statement
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while (rs.next()) {
			return rs.getInt("sum");
		}
		return 0;
	}

	@Override
	public String addUser(String userName, String password) throws SQLException {
		DBUtil dbUtil = new DBUtil();
		String sql = "select * from s_userinfo where userName=?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();

		// 检查结果集是否有记录
		if (rs.next()) {
			// name 存在返回 null
			return "user exists";
		}

		// 重新初始化 SQL 语句和 PreparedStatement
		sql = "INSERT INTO s_userinfo (userName, PASSWORD) VALUES(?,?);";
		ps = dbUtil.getPreparedStatement(sql); // 重新获取 PreparedStatement
		ps.setString(1, userName);
		ps.setString(2, password);

		// 执行插入操作，对于插入语句使用 executeUpdate
		int rowsAffected = ps.executeUpdate();
		// 添加初始头像
		String avatarSql = "UPDATE s_userinfo SET avatar = '/javaweb/img/test.jpg' WHERE userName = ?";
		PreparedStatement avatarPs = dbUtil.getPreparedStatement(avatarSql);
		avatarPs.setString(1, userName);
		int avatarRs = avatarPs.executeUpdate();
		if (avatarRs != 1) {
			return null;
		}

		// 检查是否插入成功
		if (rowsAffected > 0) {
			return "success";
		} else {
			return null;
		}
	}
	
	@Override
	public int addUser(Userinfo user) throws SQLException {
	    DBUtil dbUtil = new DBUtil();
	    
	    // 1. 调用 checkUserExists 复用用户名校验逻辑：已存在则返回0（添加失败）
	    if (checkUserExists(user.getUserName())) {
	        return 0;
	    }

	    // 2. 执行用户插入（适配 UserInfo 类字段，包含默认头像）
	    String insertSql = "INSERT INTO s_userinfo (userName, `password`, `name`, sex, age, email, avatar) VALUES(?, ?, ?, ?, ?, ?, ?);";
	    PreparedStatement insertPs = dbUtil.getPreparedStatement(insertSql);
	    
	    // 从 UserInfo 类获取字段赋值（处理空值，保持原有默认逻辑）
	    insertPs.setString(1, user.getUserName()); // 账户名称（必传）
	    insertPs.setString(2, user.getPassword()); // 密码（必传）
	    insertPs.setString(3, user.getName() != null ? user.getName() : null); // 网站名称（允许空）
	    insertPs.setString(4, user.getSex() != null ? user.getSex() : null); // 性别（允许空）
	    insertPs.setInt(5, user.getAge() != 0 ? user.getAge() : 0); // 年龄（默认0）
	    insertPs.setString(6, user.getEmail() != null ? user.getEmail() : null); // 邮箱（允许空）
	    insertPs.setString(7, "/javaweb/img/test.jpg"); // 复用原有初始头像路径

	    // 3. 执行插入并返回影响行数：1=成功，0=失败
	    int rowsAffected = insertPs.executeUpdate();
	    return rowsAffected > 0 ? rowsAffected : 0;
	}
	
	@Override
	public boolean checkUserExists(String userName) throws SQLException {
	    DBUtil dbUtil = new DBUtil();
	    String sql = "SELECT count(*) as exist_count FROM s_userinfo WHERE userName = ?";
	    Connection conn = dbUtil.getConnection();

	    PreparedStatement ps = conn.prepareStatement(sql);
	    ps.setString(1, userName);
	    ResultSet rs = ps.executeQuery();
	    
	    while (rs.next()) {
	        return rs.getInt("exist_count") > 0;
	    }
	    
	    return false;
	}
	
	public int deleteUser(String userName) throws SQLException {
	    DBUtil dbUtil = new DBUtil();
	    String sql = "DELETE FROM s_userinfo WHERE userName = ?";
	    PreparedStatement ps = dbUtil.getPreparedStatement(sql);
	    ps.setString(1, userName);

	    return ps.executeUpdate();
	}
	
//
//	@Override
//	public int EditUser(User user) throws SQLException {
//		// TODO Auto-generated method stub
//		DBUtil dbUtil = new DBUtil();
//		String sql = "UPDATE v_userinfo SET userId = ?, `name` = ?, sex = ?, age = ?, email = ? WHERE userId = ?;";
//		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
//		ps.setString(1, user.getUserId());
//		ps.setString(2, user.getName());
//		ps.setString(3, user.getSex());
//		ps.setInt(4, user.getAge());
//		ps.setString(5, user.getEmail());
//		ps.setString(6, user.getUserId());
//		int rs = ps.executeUpdate();
//		while (rs == 1) {
//			return 1;
//		}
//		return 0;
//	}
//
	@Override
	public int editPersonalPassword(Userinfo user, String newPassword) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();

		String sql = "UPDATE s_userinfo SET password = ? WHERE password = ? AND userName = ?;";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, newPassword);
		ps.setString(2, user.getPassword());
		ps.setString(3, user.getUserName());
		ps.executeUpdate();

		String countSql = "SELECT ROW_COUNT() as count";
		// 没有?占位符，用Statement
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(countSql);
		rs.next();
		if (rs.getInt("count") == 1) {
			return 1;
		}
		return 0;
	}	

	@Override
	public List<Userinfo> selectPerSonalInfo(String userName) throws SQLException {
		// TODO Auto-generated method stub
		List<Userinfo> userList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();
		String sql = "select * from s_userinfo where userName=?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Userinfo user = new Userinfo();
			user.setUserId(rs.getInt("userId"));
			user.setUserName(rs.getString("userName"));
			user.setPassword(rs.getString("password"));
			user.setRole(rs.getInt("role"));
			user.setName(rs.getString("name"));
			user.setSex(rs.getString("sex"));
			user.setAge(rs.getInt("age"));
			user.setEmail(rs.getString("email"));
			user.setAvatar(rs.getString("avatar"));
			userList.add(user);
		}
		// 返回搜索到的文章对象列表
		return userList;
	}

	@Override
	public int editPersonalInfo(Userinfo user) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil=new DBUtil();
		String sql="UPDATE s_userinfo SET `name` = ?, sex = ?, age = ?, email = ? WHERE userName = ?;";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, user.getName());
		ps.setString(2, user.getSex());
		ps.setInt(3, user.getAge());
		ps.setString(4, user.getEmail());
		ps.setString(5, user.getUserName());
		int rs = ps.executeUpdate();
		while (rs==1) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public Map<String, Userinfo> searchUserInfo(String element, String value) throws SQLException {
		// TODO Auto-generated method stub
		// 创建数据库工具类实例
		DBUtil dbUtil = new DBUtil();
		Map<String, Userinfo> userInfo=new HashMap<>();
		String sql = "SELECT * FROM s_userinfo where " + element + "=?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setObject(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Userinfo user = new Userinfo();
			user.setUserId(rs.getInt("userId"));
			user.setUserName(rs.getString("userName"));
			user.setPassword(rs.getString("password"));
			user.setRole(rs.getInt("role"));
			user.setName(rs.getString("name"));
			user.setSex(rs.getString("sex"));
			user.setAge(rs.getInt("age"));
			user.setEmail(rs.getString("email"));
			user.setAvatar(rs.getString("avatar"));
			userInfo.put(user.getUserName(), user);
		}
		return userInfo;
	}

	@Override
	public String getWebName(String userName) throws SQLException {
		// TODO Auto-generated method stub
		UserinfoDao userinfoDao=new UserinfoDaoImpl();
	    Map<String, Userinfo> userMap = userinfoDao.searchUserInfo("userName", userName);
	    if (userMap == null || userMap.isEmpty()) {
	        return null; 
	    }
	    String name = userMap.get(userName).getName();
	    if (name == null) {
	        return null;
	    }
	    
	    return name;
	}
}