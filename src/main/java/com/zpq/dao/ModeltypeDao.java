package com.zpq.dao;

import java.sql.SQLException;
import java.util.Map;

import com.zpq.pojo.Modeltype;

public interface ModeltypeDao {
	// 查询型号信息,typeId:Model
	public Map<Integer, Modeltype> searchModelInfo(Object element, Object value) throws SQLException;
}
