package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ParceiroDao;
import model.entities.Parceiro;

public class ParceiroService {
	
	private ParceiroDao dao = DaoFactory.createParceiroDao();
	
	public List<Parceiro> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Parceiro obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Parceiro obj) {
		dao.deleteById(obj.getId());
	}

}
