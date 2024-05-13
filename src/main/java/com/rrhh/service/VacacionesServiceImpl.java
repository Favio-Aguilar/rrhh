package com.rrhh.service;

import com.rrhh.Entity.Trabajador;
import com.rrhh.Entity.Vacaciones;
import com.rrhh.Repository.TrabajadorRepository;
import com.rrhh.Repository.VacacionesRepository;
import com.rrhh.dto.TrabajadorDiasTotalesDTO;
import com.rrhh.dto.VacacionesDTO;
import com.rrhh.dto.VacacionesResponseDTO;
import com.rrhh.dto.VacacionesResponseTotalesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacacionesServiceImpl implements  IVacacionesService{



    @Autowired
    TrabajadorRepository trabajadorRepository;
    @Autowired
    VacacionesRepository vacacionesRepository;
    @Override
    public VacacionesDTO agregarTrabajadorVacaciones(VacacionesDTO vacacion, Trabajador trabajador) {
        vacacion.setIdTrabajador(trabajador.getId());
        vacacion.setNombreTrabajadores(
                trabajador.getNombre());
        return vacacion;
    }

    @Override
    public VacacionesDTO registrarfechaInicioFin(VacacionesDTO vacacion, Date fechaInicio, Date fechaFin) {
        vacacion.setFechaInicio(fechaInicio);
        vacacion.setFechaFin(fechaFin);
        return vacacion;
    }

    @Override
    public VacacionesResponseDTO obtenerVacacionesPorCod(Integer codVacaciones) {
        Vacaciones vacaciones = vacacionesRepository.findById(codVacaciones).orElse(null); ///.get();
        VacacionesResponseDTO vacacionesResponseDto = ventaToVentaResponseDto(vacaciones);
        return vacacionesResponseDto;
    }

    @Override
    public List<VacacionesResponseTotalesDTO> obtenerVacacionesTotalesPorCod(Integer codTrabajador) {
        // 1. Buscar los datos del trabajador con el código especificado
        List<Object[]> resumen = vacacionesRepository.findTrabajadoresDiasTotalesPorCodigo(codTrabajador);

        // 2. Verificar si se encontraron resultados
        if (resumen.isEmpty()) {
            return null; // Retorna null si no se encontraron datos
        }

        // 3. Extraer los datos del primer resultado (asumiendo un único trabajador por código)
        Object[] fila = resumen.get(0);


        // Convertir Object[] a objetos DTO (opcional)
        List<VacacionesResponseTotalesDTO> dto = resumen.stream()
                .map(row -> new VacacionesResponseTotalesDTO(
                        (Integer) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        ((BigDecimal) row[4]).intValue()
                ))
                .collect(Collectors.toList());
       return dto;
    }




    private VacacionesResponseDTO ventaToVentaResponseDto(Vacaciones vacaciones) {
        VacacionesResponseDTO vacacionesDto = new VacacionesResponseDTO();
        vacacionesDto.setCodplanilla(vacaciones.getTrabajador().getCodplanilla());
        vacacionesDto.setIdVacaciones(vacaciones.getIdvacaciones());
        vacacionesDto.setNombreTrabajador(vacaciones.getTrabajador().getNombre());
        vacacionesDto.setApellido(vacaciones.getTrabajador().getApellido());
        vacacionesDto.setFechaInicio(vacaciones.getFechaInicio());
        vacacionesDto.setFechaFin(vacaciones.getFechaFin());
        vacacionesDto.setDiasTotales(vacaciones.getDiasTotales());
        vacacionesDto.setDiasAcumulados(vacaciones.getDiasAcumulados());
        System.out.println("DATOS DEL ARCHIVO PDF: " + vacacionesDto.getIdVacaciones()+ "//" + vacacionesDto.getDiasTotales());
        return vacacionesDto;
    }

    @Override
    public void guardarVacaciones(VacacionesDTO vacaciones) {

        // Obtener trabajador
        Trabajador trabajador = trabajadorRepository.findById(vacaciones.getIdTrabajador()).orElse(null);
        if (trabajador == null) {
            throw new RuntimeException("Trabajador no encontrado con ID: " + vacaciones.getIdTrabajador());
        }

        Vacaciones vacacionModel = new Vacaciones();
        vacacionModel.setTrabajador(trabajador);
        vacacionModel.setFechaInicio(vacaciones.getFechaInicio());
        vacacionModel.setFechaFin(vacaciones.getFechaFin());
        vacacionModel.setDiasTotales(vacaciones.getDiasTotales());


        vacacionModel.setDiasAcumulados(vacaciones.getDiasAcumulados());
        vacacionModel.setEstado(vacaciones.getEstado());




        System.out.println("DATOS REGISTRADOS EN LA BD: " + "\n" + "fecha inicio: " + vacaciones.getFechaInicio() + "\n"+"fecha fin: " + vacaciones.getFechaFin());
        Vacaciones nuevavacacion = vacacionesRepository.save(vacacionModel);


    }

    @Override
    public List<VacacionesResponseDTO> obtenerTodasLasVacaciones() {
        return vacacionesRepository.findAll()
                .stream()
                .map(vacacion -> new VacacionesResponseDTO(
                        vacacion.getTrabajador().getCodplanilla(),
                        vacacion.getIdvacaciones(),
                        vacacion.getTrabajador().getNombre() ,
                        vacacion.getTrabajador().getApellido(),
                        vacacion.getFechaInicio(),
                        vacacion.getFechaFin(),
                        vacacion.getDiasTotales(),
                        vacacion.getDiasAcumulados()))
                .collect(Collectors.toList());
    }

    @Override
    public List<VacacionesResponseDTO> obtenerVacacionesPorTrabajador(Integer idTrabajador) {


        return vacacionesRepository.findById(idTrabajador)
                .stream()
                .map(vacacion -> new VacacionesResponseDTO(
                        vacacion.getTrabajador().getCodplanilla(),
                        vacacion.getIdvacaciones(),
                        vacacion.getTrabajador().getNombre(),
                        vacacion.getTrabajador().getApellido(),
                        vacacion.getFechaInicio(),
                        vacacion.getFechaFin(),
                        vacacion.getDiasTotales(),
                        vacacion.getDiasAcumulados()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TrabajadorDiasTotalesDTO> findPaginated(Pageable pageable) {
        //final  List<TrabajadorDiasTotalesDTO> books = vacacionesRepository.findVacacionesPorPeriodo();
        final List<Object[]> resumen = vacacionesRepository.findVacacionesPorPeriodo();
        // Convertir Object[] a objetos DTO (opcional)
        List<TrabajadorDiasTotalesDTO> books = resumen.stream().map(row -> new TrabajadorDiasTotalesDTO(
                        (Integer) row[0],
                        (String) row[1],
                        (String) row[2],
                        ((BigDecimal) row[3]).intValue(),
                        ((BigDecimal) row[4]).intValue()
                ))
                .collect(Collectors.toList());
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<TrabajadorDiasTotalesDTO> list;

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        Page<TrabajadorDiasTotalesDTO> bookPage
                = new PageImpl<TrabajadorDiasTotalesDTO>(list, PageRequest.of(currentPage, pageSize), books.size());

        return bookPage;
    }


    ///
    private String obtenerFechaActual() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }
}
