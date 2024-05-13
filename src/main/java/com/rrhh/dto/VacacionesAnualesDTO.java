package com.rrhh.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VacacionesAnualesDTO {
    private Integer anio;
    private Integer diasTotales;
    private Integer diasAcumulados;
}
