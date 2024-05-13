package com.rrhh.Controller;


import com.rrhh.Entity.Hijos;
import com.rrhh.Entity.Trabajador;
import com.rrhh.Repository.HijosRepository;
import com.rrhh.Repository.TrabajadorRepository;
import com.rrhh.service.HijServiceImpl;
import com.rrhh.service.IHijoService;
import jakarta.validation.Valid;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HijosController {

    @Autowired
    HijosRepository hijosRepository;
    @Autowired
    IHijoService service;
    @Autowired
    TrabajadorRepository trabajadorRepository;

    @Autowired
    HijServiceImpl iHijoService;
    @GetMapping(value = "/vistas/Hijos/Hijos")
    public String Hijos(Model model, @RequestParam(required = false) String nombreTrabajador,@RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1); // Use 1 as default if no page is specified
        int pageSize = size.orElse(5);

        if (currentPage < 1) {
            // Handle invalid page number (e.g., redirect to the first page)
            return "redirect:/vistas/Hijos/Hijos";
        }

        List<Hijos> hijos;

        Page<Hijos> bookPage;
        if (nombreTrabajador != null && !nombreTrabajador.isEmpty()) {
            bookPage = iHijoService.findPaginated(PageRequest.of(currentPage - 1, pageSize), nombreTrabajador);
        } else {
            bookPage = iHijoService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        }


        // Get list of workers with pagination
//        Page<Hijos> bookPage;
//        try {
//            bookPage = iHijoService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
//        } catch (IllegalArgumentException e) {
//            // Handle potential exception from service (e.g., log error, redirect)
//            model.addAttribute("error", "Error: Invalid page number.");
//            bookPage = Page.empty();
//
//        }

        List<Trabajador> trabajadores = trabajadorRepository.findAll();
        model.addAttribute("hijo", bookPage.getContent());
        model.addAttribute("trabajador", trabajadores);
        model.addAttribute("bookPage", bookPage);


        // Calculate and add page numbers (ensure all pages are positive)
        int totalPages = Math.max(bookPage.getTotalPages(), 1); // Avoid negative totalPages
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);

        return "vistas/Hijos/Hijos";
    }


    @GetMapping("/vistas/Hijos/CrearHijos/{trabajadorId}")
    public String registrarHijos(Model model, @PathVariable Integer trabajadorId, RedirectAttributes redirectAttributes) {
        //id_trabajador=7
        Hijos hijo = new Hijos();
        Trabajador trabajador = trabajadorRepository.findById(trabajadorId).orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado"));
        hijo.setTrabajador(trabajador);

        int cuentanrohijo = hijosRepository.findNumeroHijosPorIdTrabajador(trabajadorId); //me devuelve 0
        System.out.println("TRABAJDOR CUENTA HIJOS : " + cuentanrohijo);
        int registranrohijo = trabajador.getNrohijos();
        System.out.println("TRABAJDOR  HIJOS registrado en su datos : " + registranrohijo);
        if (registranrohijo == 0) { //0
           redirectAttributes.addFlashAttribute("danger", "USTED NO CUENTA CON HIJOS  ");
           return "redirect:/vistas/Trabajadores/Trabajadores";
        }
        if(cuentanrohijo == registranrohijo) { //0 || 2

            redirectAttributes.addFlashAttribute("danger", "YA TIENE REGISTRADO  : " + cuentanrohijo + " HIJOS");
            return "redirect:/vistas/Trabajadores/Trabajadores";
        }


        model.addAttribute("hijo", hijo);

        return "vistas/Hijos/Registrar";
    }



    @PostMapping("/vistas/Hijos/RegistrarHijo")
    public String registrarHijos(@Valid Hijos hijo, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "vistas/Hijos/Registrar";
        }
        int innro_hijo = hijo.getTrabajador().getNrohijos();
        System.out.println("NRO DE HIJO QUE SE quiere registrar : " + innro_hijo);
        int nrohijo = hijosRepository.findNumeroHijosPorIdTrabajador(hijo.getTrabajador().getId()); //me devuelve 2
        System.out.println("NRO DE HIJO QUE TIENE REGISTRADO: " + nrohijo);

        if (nrohijo == innro_hijo){ //2==2|||0==0
            redirectAttributes.addFlashAttribute("danger", "YA TIENE REGISTRADO2 : " + nrohijo + " HIJOS");
            return "redirect:/vistas/Trabajadores/Trabajadores";
        }
        else {
            hijosRepository.save(hijo);
            redirectAttributes.addFlashAttribute("success", "Hijo(s) registrado(s) exitosamente!");
            return "redirect:/vistas/Trabajadores/Trabajadores";
        }
        //int nroHijos = hijo.getTrabajador().getNrohijos();


    }

    //UPDATE


    //DELETE
    @GetMapping("/vistas/Hijos/Eliminar/{id}")
    public String eliminarHijos(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        System.out.println("id del hijo : " + id);
        try {
            service.eliminarHijo(id);
            redirectAttributes.addFlashAttribute("success", "Hijo eliminado exitosamente!");
            return "redirect:/vistas/Hijos/Hijos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el hijo: " + e.getMessage());
        }

        return "redirect:/vistas/Hijos/Hijos";
    }



    @GetMapping("/vistas/Hijos/Editar/{cod_hijo}")
    public String actualizarhijos(@PathVariable("cod_hijo") int cod_hijo, Model model,
                                  RedirectAttributes attribute) {

        Hijos hijos = null;
        if (cod_hijo > 0) {
            hijos = service.buscarHijoPorId(cod_hijo);
            System.out.println("ID DEL TRABAJADOR: " + cod_hijo);
            // validacion a nivel de la URL
            if (hijos == null) {

                attribute.addFlashAttribute("error", "Error. El ID no existe.");
                return "redirect:/vistas/Hijos/Hijos";
            }

        } else {
            attribute.addFlashAttribute("error", "Error.");
            return "redirect:/vistas/Hijos/Hijos";
        }


        model.addAttribute("hijo", hijos);
        return "vistas/Hijos/Editar";
    }

    @PostMapping("/vistas/Hijos/UpdateHijo")
    public String updateHijos(@Valid Hijos hijo, BindingResult result, RedirectAttributes redirectAttributes) {


            hijosRepository.save(hijo);
            redirectAttributes.addFlashAttribute("success", "Hijo actualizado exitosamente!");
            return "redirect:/vistas/Trabajadores/Trabajadores";

    }

}
