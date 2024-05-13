package com.rrhh.mapper;

import com.rrhh.Entity.Condicion;
import com.rrhh.Entity.GrupoOcupacional;
import com.rrhh.Entity.Trabajador;
import com.rrhh.dto.TrabajadorDTO;



public class TrabajadorMapper {

    public static Trabajador trabajadorDTOtoTrabajador(TrabajadorDTO trabajadorDTO){
        Trabajador trabajador = new Trabajador();
        trabajador.setId(trabajadorDTO.getId_trabajador());
        trabajador.setNombre(trabajadorDTO.getNombre());
        trabajador.setApellido(trabajadorDTO.getApellido());
        trabajador.setCodplanilla(trabajadorDTO.getCodplanilla());
        trabajador.setEdad(trabajadorDTO.getEdad());
        trabajador.setCorreo(trabajadorDTO.getCorreo());
        trabajador.setDireccion(trabajadorDTO.getDireccion());
        trabajador.setFechanacimiento(trabajadorDTO.getFechanacimiento());
        trabajador.setFechaTrabajador(trabajadorDTO.getFechaTrabajador());
        trabajador.setNrodocumento(trabajadorDTO.getNrodocumento());
        trabajador.setNrohijos(trabajadorDTO.getNrohijos());
        trabajador.setSexo(trabajadorDTO.getSexo());
        trabajador.setCelular(trabajadorDTO.getCelular());


        GrupoOcupacional grupoOcupacional = new GrupoOcupacional();
        grupoOcupacional.setId(trabajadorDTO.getId_grupo_ocupacional());
        trabajador.setGrupoOcupacional(grupoOcupacional);
        //
        Condicion condicion = new Condicion();
        condicion.setId(trabajadorDTO.getId_condicion());
        trabajador.setCondicion(condicion);
        //
        trabajador.setEstado(trabajadorDTO.getEstado());
        return  trabajador;
    }




}
