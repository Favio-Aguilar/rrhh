package com.rrhh.Repository;

import com.rrhh.Entity.Condicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CondicionRepository extends JpaRepository<Condicion, Integer> {

    boolean existsByNombre(String nombre);

    List<Condicion> findAllByOrderByIdAsc();
}
