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
public class TrabajadorDTO {
    private int id_trabajador;
    private int id_rol;
    private int id_condicion;
    private int id_grupo_ocupacional;

    public String nrodocumento;

    public String codplanilla;
    public String nombre;

    public String apellido;

    public int edad;

    public String correo;

    public String clave;

    public String direccion;

    public String sexo;
    public String celular;
    public int estado;
    public int nrohijos;
    public int nroHijos;

    public Date fechanacimiento;
    public Date fechaTrabajador;

}
