package com.ipartek.formacion.perrera.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.ipartek.formacion.perrera.pojo.Perro;
import com.ipartek.formacion.perrera.util.HibernateUtil;

public class PerroDAOImpl implements PerroDAO {

	// Instancia unica para 'patron Singleton'
	private static PerroDAOImpl INSTANCE = null;

	// constructor privado para que no se pueda instanciar esta clase
	private PerroDAOImpl() {
		super();
	}

	// unico metodo para crear un objeto de esta Clase
	public synchronized static PerroDAOImpl getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PerroDAOImpl();
		}
		return INSTANCE;
	}

	@Override
	public List<Perro> getAll(String order, String campo) {

		ArrayList<Perro> perros = new ArrayList<Perro>();
		Session s = HibernateUtil.getSession();

		try {
			// controlar los QueryParam
			if("desc".equals(order)){
				perros = (ArrayList<Perro>) s.createCriteria(Perro.class).addOrder(Order.desc(campo)).list(); 
			}else{
				perros = (ArrayList<Perro>) s.createCriteria(Perro.class).addOrder(Order.asc(campo)).list();				
			}
			
//			if (order != null) {
//
//				if (!campo.equals("id") && !campo.equals("nombre") && !campo.equals("raza")) {
//					campo = "nombre";
//				}
//				if (order.equals("desc")) {
//					perros = (ArrayList<Perro>) s.createCriteria(Perro.class).addOrder(Order.desc(campo)).list();
//
//				} else if (order.equals("asc")) {
//					perros = (ArrayList<Perro>) s.createCriteria(Perro.class).addOrder(Order.asc(campo)).list();
//				}
//			}

		//Si falla porque esta mal la Query, por ejemplo una columna que no existe
		//retorno por orden descendente por id

		} catch (QueryException e) {
			perros = (ArrayList<Perro>) s.createCriteria(Perro.class).addOrder(Order.desc("id")).list();
		} finally {
			s.close();
		}
		return perros;
	}

	@Override
	public Perro getById(int idPerro) {
		Session s = HibernateUtil.getSession();
		Perro perro = null;
		try {
			s = HibernateUtil.getSession();
			s.beginTransaction();
			perro = (Perro) s.get(Perro.class, idPerro);
			s.beginTransaction().commit();
			if (perro == null) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			s.close();

		}
		return perro;
	}

	@Override
	public boolean delete(int idPerro) {
		Perro pElimnar = null;
		Session s = HibernateUtil.getSession();
		boolean resul = false;
		try {
			s.beginTransaction();
			pElimnar = (Perro) s.get(Perro.class, idPerro);
			if (pElimnar != null) {
				s.delete(pElimnar);
				s.beginTransaction().commit();
				resul = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			s.beginTransaction().rollback();
		}finally{
			s.close();
		}
		return resul;
	}

	@Override
	public boolean update(Perro perro) {
		boolean resul = false;
		Session s = HibernateUtil.getSession();
		try {
			s.beginTransaction();
			s.update(perro);
			s.beginTransaction().commit();
			resul = true;			
			
		} catch (Exception e) {
			s.beginTransaction().rollback();
		} finally {
			s.close();
		}
		return resul;
	}

	@Override
	public boolean insert(Perro perro) {

		Session s = HibernateUtil.getSession();
		boolean resul = false;

		try {

			s.beginTransaction();			
			
			int idpCreado = (Integer) s.save(perro);
			if (idpCreado > 0) {
				s.beginTransaction().commit();
				resul = true;
			} else {
				s.beginTransaction().rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
			s.beginTransaction().rollback();
		} finally {
			s.close();
		}

		return resul;
	}

}
