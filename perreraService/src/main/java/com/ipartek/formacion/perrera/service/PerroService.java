package com.ipartek.formacion.perrera.service;

import java.util.List;

import com.ipartek.formacion.perrera.pojo.Perro;

public interface PerroService {
	List<Perro> getAll(String order, String campo);

	Perro getById(long idPerro);

	boolean delete(long idPerro);

	boolean update(Perro perro);

	boolean insert(Perro perro);
}
