package com.rrhh.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HijoDTO {
    private int id_hijo;
    private int id_trabajador;
    private String nombre_hijo;
    private String apellido_hijo;
    private int edad;
    private String nro_documento;
}
