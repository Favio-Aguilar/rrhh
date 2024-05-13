package com.rrhh.Repository;

import com.rrhh.Entity.GrupoOcupacional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoOcupacionalRepository extends JpaRepository<GrupoOcupacional, Integer> {
    boolean existsByNombre(String nombre);
    // New method for the specific query
    List<GrupoOcupacional> findAllByOrderByIdAsc();

}
