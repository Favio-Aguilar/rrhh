package com.rrhh.Controller;

import com.rrhh.Entity.GrupoOcupacional;
import com.rrhh.Repository.GrupoOcupacionalRepository;
import com.rrhh.service.IGrupoOcupacionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller

public class GrupoOcupacionalController {


    @Autowired
    GrupoOcupacionalRepository funcionService;
    @Autowired
    IGrupoOcupacionalService service;
    @GetMapping("api/Funcion/listar")
    public String ListarGrupoOcupacional(Model model){

        List<GrupoOcupacional> GrupoOcupacional = funcionService.findAllByOrderByIdAsc();
        GrupoOcupacional grupoOcupacional = new GrupoOcupacional();
        model.addAttribute("funciones", grupoOcupacional);

        model.addAttribute( "grupoOcupacional", GrupoOcupacional);

        return "vistas/GrupoOcupacional/GrupoOcupacional";
    }

    @GetMapping("/vistas/Funcion/Registrar")
    public String registrarGrupoOcupacional(Model model) {

        GrupoOcupacional grupoOcupacional = new GrupoOcupacional();
        model.addAttribute("funcion", grupoOcupacional);

        return "redirect:/api/Funcion/listar";
    }

    @PostMapping(value ="/vistas/Funcion/RegistrarFuncion")
    public String registrarGrupoOcupacional(@Valid GrupoOcupacional grupoOcupacional, BindingResult result, RedirectAttributes redirectAttributes) {
        // Verificar si el nombre ya existe en la base de datos
        boolean nombreExistente = funcionService.existsByNombre(grupoOcupacional.getNombre());

        if (nombreExistente) {
            // Agregar error de nombre duplicado al BindingResult
            redirectAttributes.addFlashAttribute("mensaje", "Este nombre ya existe");
            return "redirect:/api/Funcion/listar"; // Mostrar la vista de registro con el error
        }

        // Guardar la función si el nombre no existe
        funcionService.save(grupoOcupacional);

        // Agregar mensaje de éxito al RedirectAttributes
        redirectAttributes.addFlashAttribute("success", "Función registrada exitosamente!");


        return "redirect:/api/Funcion/listar";
    }

}