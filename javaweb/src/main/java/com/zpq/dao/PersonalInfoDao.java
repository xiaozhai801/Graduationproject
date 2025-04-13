package com.zpq.dao;

import java.sql.SQLException;
import java.util.List;

public interface PersonalInfoDao {
	public List<Object> SelectPerSonalInfo(String name) throws SQLException;
}
