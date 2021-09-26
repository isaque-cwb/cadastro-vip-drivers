package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.ParceiroDaoJDBC;

public class DaoFactory {

	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}

	public static ParceiroDao createParceiroDao() {
		return new ParceiroDaoJDBC(DB.getConnection());
	}
}
