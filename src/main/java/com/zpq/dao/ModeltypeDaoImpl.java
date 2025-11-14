package com.zpq.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.zpq.pojo.Modeltype;
import com.zpq.utils.DBUtil;

public class ModeltypeDaoImpl implements ModeltypeDao {

	@Override
	public Map<Integer, Modeltype> searchModelInfo(Object element, Object value) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil dbUtil = new DBUtil();
		Modeltype model = new Modeltype();
		Map<Integer, Modeltype> modelInfo=new HashMap<>();
		String sql = "SELECT * FROM v_modeltype where " + element + "=?";
		// 获取预编译的SQL语句对象
		PreparedStatement ps = dbUtil.getPreparedStatement(sql);
		ps.setObject(1, value);
		// 执行SQL查询，并获取结果集
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			model.setTypeId(rs.getInt("typeId"));
			model.setTypeName(rs.getString("typeName"));
			modelInfo.put(model.getTypeId(), model);
		}
		// 返回查询到的型号对象列表
		return modelInfo;
	}

}
