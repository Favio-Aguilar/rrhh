package com.rrhh.Repository;

import com.rrhh.Entity.Hijos;
import com.rrhh.Entity.Trabajador;
import com.rrhh.dto.TrabajadorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HijosRepository extends JpaRepository<Hijos, Integer> {

    List<Hijos> findByTrabajador_Nombre(String nombreTrabajador);
    // Custom implementation using Criteria API (optional)
    @Query("SELECT h FROM Hijos h JOIN h.trabajador t WHERE t.nombre LIKE :nombre")
    Page<Hijos> findHijosByTrabajadorNombreContaining(String nombre, Pageable pageable);


  @Query(value = "SELECT COUNT(h.id) AS nro_hijos " +
        "FROM Hijos h LEFT JOIN Trabajador t ON h.id_trabajador = t.id_trabajador " +
        "GROUP BY t.id_trabajador, t.nombre, t.apellido", nativeQuery = true)
    List<Integer> findNumeroHijosPorTrabajador();

    @Query(value = "SELECT COUNT(*) AS NUMERO_DE_HIJOS FROM hijos WHERE id_trabajador = :idTrabajador", nativeQuery = true)
    int findNumeroHijosPorIdTrabajador(@Param("idTrabajador") Integer idTrabajador);

}
