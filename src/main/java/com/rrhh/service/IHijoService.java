package com.rrhh.service;

import com.rrhh.Entity.Hijos;
import com.rrhh.Entity.Trabajador;
import com.rrhh.dto.TrabajadorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHijoService {

    List <Hijos> buscarByNombreTrabajador(String nombre);
    void guardarHijo(Hijos hijo);

    public Hijos buscarHijoPorId(int cod_hijo);
    void eliminarHijo(Integer id);
    List<Integer> getNumeroHijosPorTrabajador();
   public Page<Hijos> findPaginated(Pageable pageable);


    Page<Hijos> findPaginated(Pageable pageable, String nombreTrabajador);
}
