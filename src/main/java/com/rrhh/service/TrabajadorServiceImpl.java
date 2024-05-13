package com.rrhh.service;

import com.rrhh.Entity.Trabajador;
import com.rrhh.Repository.HijosRepository;
import com.rrhh.Repository.TrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
@Service
public class TrabajadorServiceImpl  implements  ITrabajadorService{

    @Autowired
    TrabajadorRepository trabajadorRepository;

    @Autowired
    HijosRepository hijosRepository;

    @Override
    public void guardarTrabajador(Trabajador trabajador) {
        trabajador.setEstado(1);

        //trabajador.setFechaIngreso(new Date(obtenerFechaActual()));
        trabajadorRepository.save(trabajador);
    }

    private String obtenerFechaActual() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
       String formattedDateTime = now.format(formatter);


        return formattedDateTime;
    }
    public int calcularEdad(Trabajador trabajador){
        Date fechaNacimiento = trabajador.getFechanacimiento();
        Date fechaActual = new Date();
        long milisegundosDiferencia = fechaActual.getTime() - fechaNacimiento.getTime();
        long segundosDiferencia = milisegundosDiferencia / 1000;
        long minutosDiferencia = segundosDiferencia / 60;
        long horasDiferencia = minutosDiferencia / 60;
        long diasDiferencia = horasDiferencia / 24;
        long añosDiferencia = (diasDiferencia / 365);
        return (int) añosDiferencia;
    }

    @Override
    public List<Trabajador> obtenerTrabajadores() {
        return trabajadorRepository.findAll();
    }

    @Override
    public Trabajador buscarTrabajadoroPorId(int cod_trabajador) {
        return trabajadorRepository.findById(cod_trabajador).orElse(null);
    }

    @Override
    public void eliminarTrabajador(int cod_trabajador) {
        Trabajador trab;
        trab = trabajadorRepository.findById(cod_trabajador).orElse(null);

        if(trab != null){
            trab.setEstado(0);
            trabajadorRepository.save(trab);
        }
    }

    @Override
    public Page<Trabajador> findPaginated(Pageable pageable) {

        final  List<Trabajador> books = trabajadorRepository.findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Trabajador> list;

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        Page<Trabajador> bookPage
                = new PageImpl<Trabajador>(list, PageRequest.of(currentPage, pageSize), books.size());

        return bookPage;
    }

    @Override
    public Trabajador buscarTrabajadorPorNroDocumento(String nroDocumento) {
        return trabajadorRepository.findTrabajadoresByNrodocumento(nroDocumento);
    }
}
