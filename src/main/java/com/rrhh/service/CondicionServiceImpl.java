package com.rrhh.service;

import com.rrhh.Entity.Condicion;
import com.rrhh.Repository.CondicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CondicionServiceImpl implements ICondicionService{

    @Autowired
    CondicionRepository repo;
    @Override
    public List<Condicion> ListarCondiones() {
        return repo.findAll();
    }

    @Override
    public void guardarCondicion(Condicion condicion) {
        repo.save(condicion);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return repo.existsByNombre(nombre);
    }
}
