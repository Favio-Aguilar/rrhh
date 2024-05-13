package com.rrhh.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrabajadorDiasTotalesDTO {
    private Integer idTrabajador;
    private String Periodo;
    private String nombre;
    private Integer diasTotales;
    private Integer diasAcumulados;
    //private Integer estado;
}
