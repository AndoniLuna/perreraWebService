package com.ipartek.formacion.perrera.service;

import java.util.List;

import com.ipartek.formacion.perrera.dao.PerroDAO;
import com.ipartek.formacion.perrera.dao.PerroDAOImpl;
import com.ipartek.formacion.perrera.pojo.Perro;

/**
 * 
 * @author JHM
 *
 */
public class PerreraServiceImpl implements PerreraService {

	private static PerreraServiceImpl INSTANCE = null;
	private PerroDAO perroDAO;

	/**
	 * 
	 */
	private PerreraServiceImpl() {
		perroDAO = PerroDAOImpl.getInstance();
	}

	/**
	 * 
	 * @return INSTANCE
	 */
	public static PerreraServiceImpl getInstance() {
		if (INSTANCE == null) {
			createInstance();
		}
		return INSTANCE;
	}

	/**
	 * 
	 */
	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PerreraServiceImpl();
		}
	}

	@Override()
	public List<Perro> getAll(String order, String campo) {
		return perroDAO.getAll(order, campo);
	}

	@Override()
	public Perro getById(long idPerro) {
		return perroDAO.getById(idPerro);
	}

	@Override()
	public boolean delete(long idPerro) {
		return perroDAO.delete(idPerro);
	}

	@Override()
	public boolean update(Perro perro) {
		return perroDAO.update(perro);
	}

	@Override()
	public boolean insert(Perro perro) {
		return perroDAO.insert(perro);
	}

}
