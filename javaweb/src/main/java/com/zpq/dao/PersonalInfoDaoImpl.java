package com.zpq.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zpq.pojo.User;
import com.zpq.utils.DBUtil;

public class PersonalInfoDaoImpl implements PersonalInfoDao {

	@Override
	public List<Object> SelectPerSonalInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		List<Object> userList = new ArrayList<>();
		DBUtil dbUtil = new DBUtil();

		String sql = "select * from v_userinfo where userId=?";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			User user = new User();
			user.setUserId(rs.getString("userId"));
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
	public int EditPersonalInfo(User user) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil=new DBUtil();
		String sql="UPDATE v_userinfo SET userId = ?, `name` = ?, sex = ?, age = ?, email = ? WHERE userId = ?;";
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setString(1, user.getUserId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getSex());
		ps.setInt(4, user.getAge());
		ps.setString(5, user.getEmail());
		ps.setString(6, user.getUserId());
		int rs = ps.executeUpdate();
		while (rs==1) {
			return 1;
		}
		return 0;
	}

}
