package com.rrhh.service;

import com.rrhh.Entity.Condicion;

import java.util.List;

public interface ICondicionService {

    List<Condicion> ListarCondiones();
    void guardarCondicion(Condicion condicion);
     boolean existsByNombre(String nombre);
}
