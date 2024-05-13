package com.rrhh.service;

import com.rrhh.Entity.Trabajador;
import com.rrhh.Entity.Vacaciones;
import com.rrhh.dto.TrabajadorDiasTotalesDTO;
import com.rrhh.dto.VacacionesDTO;
import com.rrhh.dto.VacacionesResponseDTO;
import com.rrhh.dto.VacacionesResponseTotalesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface IVacacionesService {

    VacacionesDTO agregarTrabajadorVacaciones(VacacionesDTO vacacion, Trabajador trabajador);
    VacacionesDTO registrarfechaInicioFin(VacacionesDTO vacacion, Date fechaInicio, Date fechaFin);
    VacacionesResponseDTO obtenerVacacionesPorCod(Integer codVacaciones);
    List<VacacionesResponseTotalesDTO> obtenerVacacionesTotalesPorCod(Integer codTrabajador);

    //public List<VacacionesDTO> findVacacionesidTrabajadorANDYear(int idtrabajador, int anio);

    void guardarVacaciones(VacacionesDTO vacaciones);

    public List<VacacionesResponseDTO> obtenerTodasLasVacaciones();
    List<VacacionesResponseDTO> obtenerVacacionesPorTrabajador(Integer idTrabajador);
    public Page<TrabajadorDiasTotalesDTO> findPaginated(Pageable pageable);

}
