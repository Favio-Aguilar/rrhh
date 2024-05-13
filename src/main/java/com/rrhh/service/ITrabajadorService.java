package com.rrhh.service;

import com.rrhh.Entity.Hijos;
import com.rrhh.Entity.Trabajador;
import com.rrhh.dto.TrabajadorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITrabajadorService {

    void guardarTrabajador(Trabajador trabajador);


    List<Trabajador> obtenerTrabajadores();
    public Trabajador buscarTrabajadoroPorId(int cod_trabajador);
    public void eliminarTrabajador(int cod_trabajador);
   // List<ProductoDto> obtenerProductosApi(Integer idCategoria, String nombreProducto);

    public Page<Trabajador> findPaginated(Pageable pageable);

    Trabajador buscarTrabajadorPorNroDocumento(String nroDocumento);


}
