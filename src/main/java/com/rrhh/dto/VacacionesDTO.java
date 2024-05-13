package com.rrhh.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class VacacionesDTO {
    private  int idVacaciones;
    private int idTrabajador;

    private String nombreTrabajadores;

    //private String fecharegistrada;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer diasTotales;
    private Integer diasAcumulados;
    private Integer estado;





}
