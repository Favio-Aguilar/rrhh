package com.rrhh.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VacacionesResponseDTO {

    private String codplanilla;
    private int idVacaciones;


    private String nombreTrabajador;

    private String apellido;

    private Date fechaInicio;

    private Date fechaFin;

    private  Integer diasTotales;
    private Integer diasAcumulados;
}
