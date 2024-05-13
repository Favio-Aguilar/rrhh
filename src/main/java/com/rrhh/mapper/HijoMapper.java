package com.rrhh.mapper;

import com.rrhh.Entity.Hijos;
import com.rrhh.Entity.Trabajador;
import com.rrhh.dto.HijoDTO;

public class HijoMapper {
    public static Hijos hijoDTOtoHijo(HijoDTO hijoDTO){
        Hijos hijo = new Hijos();
        hijo.setId(hijoDTO.getId_hijo());
        hijo.setNombre_hijo(hijoDTO.getNombre_hijo());
        hijo.setApellido_hijo(hijoDTO.getApellido_hijo());
        hijo.setEdad(hijoDTO.getEdad());
        hijo.setNro_documento(hijoDTO.getNro_documento());

        Trabajador trabajador = new Trabajador();
        trabajador.setId(hijoDTO.getId_trabajador());
        //hijo.setTrabajador(trabajador);

        return  hijo;
    }
}
