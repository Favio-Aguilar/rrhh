package com.rrhh.Repository;

import com.rrhh.Entity.Vacaciones;

import com.rrhh.dto.VacacionesIdAnio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VacacionesRepository extends JpaRepository<Vacaciones, Integer> {
    List<Vacaciones> findByTrabajadorId(int idTrabajador);

    @Query(value = "SELECT trabajador.id_Trabajador, trabajador.codplanilla, trabajador.nombre, trabajador.apellido, vacaciones.dias_totales, vacaciones.estado " +
            "FROM vacaciones " +
            "INNER JOIN trabajador ON trabajador.id_Trabajador = vacaciones.id_Trabajador " +
            "GROUP BY trabajador.id_Trabajador, trabajador.codplanilla, trabajador.nombre, trabajador.apellido,vacaciones.estado, vacaciones.dias_totales", nativeQuery = true)
    List<Object[]> findTrabajadoresDiasTotales();

    @Query(value = "SELECT * FROM vacaciones WHERE id_trabajador = :idTrabajador AND year(fecha_inicio) = :anio ORDER BY fecha_inicio DESC LIMIT 1", nativeQuery = true)
    public Vacaciones findVacacionesPorTrabajadorYAnio(@Param("idTrabajador") int idTrabajador, @Param("anio") int anio);

    @Query(value = "SELECT * FROM vacaciones WHERE id_trabajador = :idtrabajador AND month(fecha_inicio) = :mes  and year(fecha_inicio) = :anio", nativeQuery = true)
    public Vacaciones findVacacionesPorTrabajadorMesYAnioNative(
            @Param("idtrabajador") int idTrabajador,
            @Param("mes") int mes,
            @Param("anio") int anio);
    @Query(value = "SELECT EXTRACT(YEAR FROM fecha_inicio) AS anio, SUM(dias_totales) AS diastotales, Sum(dias_acumulados) as diasacumulados " +
            "FROM vacaciones " +
            "WHERE id_trabajador = :idTrabajador AND EXTRACT(YEAR FROM fecha_inicio) = :anio " +
            "GROUP BY EXTRACT(YEAR FROM fecha_inicio) ", nativeQuery = true)

    public VacacionesIdAnio findVacacionesPorTrabajadorYAnioAgrupadoPrimero(
            @Param("idTrabajador") int idTrabajador,
            @Param("anio") int anio);
    @Query(value = "SELECT\n" +
            "  v.id_trabajador,CONCAT(YEAR(v.fecha_inicio), '-', YEAR(DATE_ADD(v.fecha_inicio, INTERVAL 1 YEAR))) AS PERIODO,\n" +
            "  t.nombre,\n" +
            "  SUM(v.dias_totales) AS diasTotales,\n" +
            "  SUM(v.dias_acumulados) AS diasAcumulados\n" +
            "FROM vacaciones AS v\n" +
            "INNER JOIN trabajador t ON t.id_trabajador = v.id_trabajador\n" +
            "WHERE YEAR(v.fecha_inicio) < YEAR(DATE_ADD(v.fecha_inicio, INTERVAL 1 YEAR))\n" +
            "GROUP BY CONCAT(YEAR(v.fecha_inicio), '-', YEAR(DATE_ADD(v.fecha_inicio, INTERVAL 1 YEAR))), t.nombre, v.id_trabajador;", nativeQuery = true)
    List<Object[]> findVacacionesPorPeriodo();


    @Query(value = "SELECT trabajador.id_Trabajador, trabajador.codplanilla, trabajador.nombre, trabajador.apellido, SUM(vacaciones.dias_totales) AS diasTotales " +
            "FROM vacaciones " +
            "INNER JOIN trabajador ON trabajador.id_Trabajador = vacaciones.id_Trabajador " +
            "WHERE trabajador.id_Trabajador = :codigoTrabajador " +
            "GROUP BY trabajador.id_Trabajador, trabajador.codplanilla, trabajador.nombre, trabajador.apellido " +
            "ORDER BY diasTotales DESC", nativeQuery = true)
    List<Object[]> findTrabajadoresDiasTotalesPorCodigo(@Param("codigoTrabajador") int codigoTrabajador);





    @Query(value = "SELECT v.id_trabajador, CONCAT(YEAR(v.fecha_inicio), '-', YEAR(DATE_ADD(v.fecha_inicio, INTERVAL 1 YEAR))) AS periodo, " +
            "t.nombre, SUM(v.dias_totales) AS totalDiasTotales, SUM(v.dias_acumulados) AS totalDiasAcumulados " +
            "FROM vacaciones AS v " +
            "INNER JOIN trabajador t ON t.id_trabajador = v.id_trabajador " +
            "WHERE YEAR(v.fecha_inicio) < YEAR(DATE_ADD(v.fecha_inicio, INTERVAL 1 YEAR)) " +
            "AND t.nombre LIKE %:nombre% GROUP BY v.id_trabajador, CONCAT(YEAR(v.fecha_inicio), '-', YEAR(DATE_ADD(v.fecha_inicio, INTERVAL 1 YEAR))), t.nombre", nativeQuery = true)
    List<Object[]> findVacacionesPorTrabajadorNombreYPeriodo(@Param("nombre")String nombre);
}
