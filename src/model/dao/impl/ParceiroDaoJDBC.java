package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import db.DB;
import db.DbException;
import model.dao.ParceiroDao;
import model.entities.Department;
import model.entities.Parceiro;

public class ParceiroDaoJDBC implements ParceiroDao {

	private Connection conn;
	
	public ParceiroDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Parceiro obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO parceiro "
					+ "(Nome, Telefone, Email, BirthDate, Endereco, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getTelefone());
			st.setString(3, obj.getEmail());
			st.setDate(4, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setString(5, obj.getEndereco());
			st.setInt(6, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! Nenhuma Linha Afetada!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Parceiro obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE parceiro "
					+ "SET Nome = ?, Telefone = ?, Email = ?, BirthDate = ?, Endereco = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getTelefone());
			st.setString(3, obj.getEmail());
			st.setDate(4, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setString(5, obj.getEndereco());
			st.setInt(6, obj.getDepartment().getId());
			st.setInt(7, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM parceiro WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Parceiro findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT parceiro.*,department.Nome as DepNome "
					+ "FROM parceiro INNER JOIN department "
					+ "ON parceiro.DepartmentId = department.Id "
					+ "WHERE parceiro.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Parceiro obj = instantiateParceiro(rs, dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Parceiro instantiateParceiro(ResultSet rs, Department dep) throws SQLException {
		Parceiro obj = new Parceiro();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("Nome"));
		obj.setTelefone(rs.getString("Telefone"));
		obj.setEmail(rs.getString("Email"));
		obj.setEndereco(rs.getString("Endereco"));
		obj.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Parceiro> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT parceiro.*,department.Name as DepName "
					+ "FROM parceiro INNER JOIN department "
					+ "ON parceiro.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Parceiro> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Parceiro obj = instantiateParceiro(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Parceiro> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT parceiro.*,department.Nome as DepNome "
					+ "FROM parceiro INNER JOIN department "
					+ "ON parceiro.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Nome");
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Parceiro> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Parceiro obj = instantiateParceiro(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
