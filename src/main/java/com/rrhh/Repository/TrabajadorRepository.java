package com.rrhh.Repository;

import com.rrhh.Entity.Trabajador;

import com.rrhh.dto.TrabajadorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TrabajadorRepository extends JpaRepository <Trabajador, Integer> {


    @Query("SELECT t FROM Trabajador t WHERE t.nombre = :nombre")
    List<Trabajador> findTrabajadoresByNombre(@Param("nombre") String nombre);


    @Query(value = "SELECT * FROM trabajador t WHERE t.nrodocumento = :nrodocumento" , nativeQuery = true)
    Trabajador findTrabajadoresByNrodocumento(@Param("nrodocumento") String nrodocumento);


}
