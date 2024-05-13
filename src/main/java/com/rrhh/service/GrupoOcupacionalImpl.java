package com.rrhh.service;

import com.rrhh.Entity.GrupoOcupacional;
import com.rrhh.Repository.GrupoOcupacionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoOcupacionalImpl implements IGrupoOcupacionalService {

    @Autowired
    GrupoOcupacionalRepository repo;
    @Override
    public List<GrupoOcupacional> ListarFunciones() {
        return repo.findAllByOrderByIdAsc();
    }

    @Override
    public void guardarGrupoOcupacional(GrupoOcupacional grupoOcupacional) {
        repo.save(grupoOcupacional);
    }

    @Override
    public boolean existeNombreGrupoOcupacional(String nombreFuncion) {
        return repo.existsByNombre(nombreFuncion);
    }
}
