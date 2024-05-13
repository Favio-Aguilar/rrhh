package com.rrhh.service;

import com.rrhh.Entity.Hijos;
import com.rrhh.Entity.Trabajador;
import com.rrhh.Repository.HijosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class HijServiceImpl implements  IHijoService{

    @Autowired
    HijosRepository hijosRepository;


    @Override
    public List<Hijos> buscarByNombreTrabajador(String nombre) {
        return hijosRepository.findByTrabajador_Nombre(nombre);
    }

    @Override
    public void guardarHijo(Hijos hijo) {
        hijo.getTrabajador().getNrohijos();



    }

    @Override
    public Hijos buscarHijoPorId(int cod_hijo) {
        return hijosRepository.findById(cod_hijo).orElse(null);
    }

    @Override
    public void eliminarHijo(Integer id) {
        hijosRepository.deleteById(id);
    }

    @Override
    public List<Integer> getNumeroHijosPorTrabajador() {
        return hijosRepository.findNumeroHijosPorTrabajador();
    }

    @Override
    public Page<Hijos> findPaginated(Pageable pageable) {
        final  List<Hijos> books = hijosRepository.findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Hijos> list;

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        Page<Hijos> bookPage
                = new PageImpl<Hijos>(list, PageRequest.of(currentPage, pageSize), books.size());

        return bookPage;
    }

    @Override
    public Page<Hijos> findPaginated(Pageable pageable, String nombreTrabajador) {
        // Handle case with no search term
        if (nombreTrabajador == null || nombreTrabajador.isEmpty()) {
            return hijosRepository.findAll(pageable);
        }

        // Search with criteria based on nombreTrabajador
        Page<Hijos> bookPage = hijosRepository.findHijosByTrabajadorNombreContaining(nombreTrabajador, pageable);

        return bookPage;
    }



}
