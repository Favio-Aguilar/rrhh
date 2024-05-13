package com.rrhh.service;

import com.rrhh.Entity.GrupoOcupacional;

import java.util.List;

public interface IGrupoOcupacionalService {

    List<GrupoOcupacional> ListarFunciones();
    void guardarGrupoOcupacional(GrupoOcupacional grupoOcupacional);
    public boolean existeNombreGrupoOcupacional(String nombreFuncion);
}
