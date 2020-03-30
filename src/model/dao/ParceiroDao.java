package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Parceiro;

public interface ParceiroDao {

	void insert(Parceiro obj);
	void update(Parceiro obj);
	void deleteById(Integer id);
	Parceiro findById(Integer id);
	List<Parceiro> findAll();
	List<Parceiro> findByDepartment(Department department);
}
