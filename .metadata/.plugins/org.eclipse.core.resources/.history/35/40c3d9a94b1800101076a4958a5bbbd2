package com.zpq.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.zpq.pojo.User;
import com.zpq.utils.DBUtil;

public class EditPersonalInfoDaoImpl implements EditPersonalInfoDao {

	@Override
	public int EditUser(User user) throws SQLException {
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
