package com.rrhh.Controller;

import com.rrhh.Entity.Condicion;
import com.rrhh.Entity.GrupoOcupacional;
import com.rrhh.Entity.Trabajador;
import com.rrhh.Repository.CondicionRepository;
import com.rrhh.Repository.GrupoOcupacionalRepository;
import com.rrhh.Repository.TrabajadorRepository;
import com.rrhh.service.ITrabajadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TrabajadorController {

    @Autowired
    ITrabajadorService iTrabajadorService;

    @Autowired
    TrabajadorRepository trabajadorRepository;
    @Autowired
    GrupoOcupacionalRepository funcionRepository;
    @Autowired
    CondicionRepository condicionRepository;

    private static final int PAGE_SIZE = 10; // Define el tamaño de página (ej: 10 trabajadores por página)

    @GetMapping(value = "/vistas/Trabajadores/Trabajadores")
    public String Trabajadores(Model model, @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1); // Use 1 as default if no page is specified
        int pageSize = size.orElse(5);

        if (currentPage < 1) {
            // Handle invalid page number (e.g., redirect to the first page)
            return "redirect:/vistas/Trabajadores/Trabajadores";
        }

        // Get list of workers with pagination
        Page<Trabajador> bookPage;
        try {
            bookPage = iTrabajadorService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        } catch (IllegalArgumentException e) {
            // Handle potential exception from service (e.g., log error, redirect)
            model.addAttribute("error", "Error: Invalid page number.");
            bookPage = Page.empty();

        }

        // Get list of functions and conditions
        List<GrupoOcupacional> funciones = funcionRepository.findAll();
        List<Condicion> condiciones = condicionRepository.findAll();

        // Prepare data for the view
        model.addAttribute("trabajador", bookPage.getContent());
        model.addAttribute("grupoOcupacional", funciones);
        model.addAttribute("condicion", condiciones);
        model.addAttribute("bookPage", bookPage);

        // Calculate and add page numbers (ensure all pages are positive)
        int totalPages = Math.max(bookPage.getTotalPages(), 1); // Avoid negative totalPages
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);

        return "vistas/Trabajadores/Trabajadores";
    }


    // CREATE Productos - GET
    @GetMapping(value = "/vistas/Trabajadores/Registrar")
    public String registrarTrabajadores(Model model) {
        Trabajador trabajadores = new Trabajador();
        List<GrupoOcupacional> funciones = funcionRepository.findAll();
        List<Condicion> condiciones = condicionRepository.findAll();
        model.addAttribute("trabajador", trabajadores);
        model.addAttribute("grupoOcupacional", funciones);
        model.addAttribute("condicion", condiciones);
        return "vistas/Trabajadores/Registrar";
    }

    // CREATE Productos - POST
    @PostMapping(value = "/vistas/Trabajadores/Registrar")
    public String registrarTrabajadores(@Valid @ModelAttribute Trabajador trabajadores, BindingResult result, Model model,
                                     RedirectAttributes redirectAttributes) {
        List<GrupoOcupacional> funciones = funcionRepository.findAll();
        List<Condicion> condiciones = condicionRepository.findAll();
        int edad = calcularEdad(trabajadores);//0
        trabajadores.setEdad(edad);

        if (trabajadores.getEdad() < 18 ) {
            result.addError(new FieldError("trabajador", "fechanacimiento",
                    "El trabajador debe ser mayor de 18 años."));
            model.addAttribute("trabajador", trabajadores);
            model.addAttribute("grupoOcupacional", funciones);
            model.addAttribute("condicion", condiciones);
            System.out.println("ERRROR " + result.hasErrors()  + trabajadores.getNombre() + "edad: " + trabajadores.getEdad());
            return "/vistas/Trabajadores/Registrar";
        }
        else if(result.hasErrors()){
            model.addAttribute("trabajador", trabajadores);
            model.addAttribute("grupoOcupacional", funciones);
            model.addAttribute("condicion", condiciones);
            return "/vistas/Trabajadores/Registrar";
        }else {
            model.addAttribute("trabajador", trabajadores);
            iTrabajadorService.guardarTrabajador(trabajadores);
            redirectAttributes.addFlashAttribute("success", " Trabajador REGISTRADO exitosamente.");
            return "redirect:/vistas/Trabajadores/Trabajadores";
        }

    }

    /*calcular edad*/
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

    // UPDATE TRABAJADORES
    @GetMapping("/vistas/Trabajadores/Editar/{cod_trabajador}")
    public String actualizarTrabajadores(@PathVariable("cod_trabajador") int cod_trabajador,Model model,
                                      RedirectAttributes attribute) {

            Trabajador trabajadores = null;
            if (cod_trabajador > 0) {
                trabajadores = iTrabajadorService.buscarTrabajadoroPorId(cod_trabajador);
                System.out.println("ID DEL TRABAJADOR: " + cod_trabajador);
            } else {
                attribute.addFlashAttribute("error", "Error.");
                return "redirect:/vistas/Trabajadores/Trabajadores";
                   }
                List<GrupoOcupacional> funciones = funcionRepository.findAll();
                List<Condicion> condiciones = condicionRepository.findAll();
                model.addAttribute("grupoOcupacional", funciones);
                model.addAttribute("condicion", condiciones);
                model.addAttribute("trabajador", trabajadores);
        return "vistas/Trabajadores/Editar";
    }


    @PostMapping(value = "/vistas/Trabajadores/Actualizar")
    public String updateTrabajadores(@Valid @ModelAttribute Trabajador trabajadores, BindingResult result, Model model,
                                        RedirectAttributes redirectAttributes) {
        List<GrupoOcupacional> funciones = funcionRepository.findAll();
        List<Condicion> condiciones = condicionRepository.findAll();
        int edad = calcularEdad(trabajadores);//0
        trabajadores.setEdad(edad);


            if (trabajadores.getEdad() < 18 ) {
                result.addError(new FieldError("trabajador", "fechanacimiento",
                        "El trabajador debe ser mayor de 18 años."));
                model.addAttribute("trabajador", trabajadores);
                model.addAttribute("grupoOcupacional", funciones);
                model.addAttribute("condicion", condiciones);
                System.out.println("ERRROR " + result.hasErrors()  + trabajadores.getNombre() + "edad: " + trabajadores.getEdad());
                return "/vistas/Trabajadores/Editar";
            }
            else if (trabajadores.getEdad() == 0){
                result.addError(new FieldError("trabajador", "fechanacimiento",
                        "La Fecha de Nacimiento del trabajador invalido"));
                model.addAttribute("trabajador", trabajadores);
                model.addAttribute("grupoOcupacional", funciones);
                model.addAttribute("condicion", condiciones);
                System.out.println("ERRROR " + result.hasErrors()  + trabajadores.getNombre() + "edad: " + trabajadores.getEdad());
                return "/vistas/Trabajadores/Editar";
            } else {
            model.addAttribute("trabajador", trabajadores);
            iTrabajadorService.guardarTrabajador(trabajadores);
            redirectAttributes.addFlashAttribute("success", " Trabajador ACTUALIZADO exitosamente.");
            return "redirect:/vistas/Trabajadores/Trabajadores";
        }


    }

    // Eliminado lógico
    @GetMapping("/vistas/Trabajadores/Eliminar/{id}")
    public String eliminarTrabajadores(@PathVariable("id") int cod_trabajador, Model model,
                                   RedirectAttributes redirectAttributes) {
        Trabajador trabajadores = null;
        if (cod_trabajador > 0) {
            trabajadores = iTrabajadorService.buscarTrabajadoroPorId(cod_trabajador);

        } else {
            redirectAttributes.addFlashAttribute("error", "Error.");
            return "redirect:/vistas/Trabajadores/Trabajadores";
        }
        iTrabajadorService.eliminarTrabajador(cod_trabajador);
            return "redirect:/vistas/Trabajadores/Trabajadores";
    }

    //


}
