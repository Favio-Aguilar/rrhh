package com.rrhh.Controller;

import com.rrhh.Entity.Condicion;
import com.rrhh.Repository.CondicionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller

public class CondicionController {

    @Autowired
    CondicionRepository condicionRepository;

    @GetMapping("api/Condicion/listar")
   public String ListarCondion(Model model){

        List<Condicion> condiciones = condicionRepository.findAllByOrderByIdAsc();

        Condicion condicion = new Condicion();
        model.addAttribute("condiciones", condicion);

        model.addAttribute( "condicion", condiciones);

        return "vistas/Condicion/Condiciones";
    }

    @GetMapping("/vistas/Condicion/Registrar")
    public String registrarCondiciones(Model model) {

        Condicion condicion = new Condicion();
        model.addAttribute("condicion", condicion);

        return "redirect:/api/Condicion/listar";
    }

    @PostMapping("/vistas/Condicion/RegistrarCondicion")
    public String registrarCondiciones(@Valid Condicion condicion, BindingResult result, RedirectAttributes redirectAttributes) {

        // Verificar si el nombre ya existe en la base de datos
        boolean nombreExistente = condicionRepository.existsByNombre(condicion.getNombre());

        if (nombreExistente) {
            // Agregar error de nombre duplicado al BindingResult
            redirectAttributes.addFlashAttribute("mensaje", "Este nombre ya existe");
            return "redirect:/api/Condicion/listar"; // Mostrar la vista de registro con el error
        }

        // Guardar la función si el nombre no existe
        condicionRepository.save(condicion);

        // Agregar mensaje de éxito al RedirectAttributes
        redirectAttributes.addFlashAttribute("mensaje", "Condicion registrada exitosamente!");
        return "redirect:/api/Condicion/listar";
    }


}
