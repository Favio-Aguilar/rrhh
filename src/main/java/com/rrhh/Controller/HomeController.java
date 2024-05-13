package com.rrhh.Controller;

import com.rrhh.Entity.Rol;
import com.rrhh.Entity.Usuario;
import com.rrhh.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private IUserRepository iUserRepository;

    @GetMapping({ "/", "login" })
    public String login() {
        return "formlogin";
    }


    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/save")
    public String save() {
        Usuario user = new Usuario();
        Rol role = new Rol();
        role.setId_rol(1);
        user.setNombreUsuario("Favio");
        user.setCorreoUsuario("Administrador@gmail.com");
        user.setClaveUsuario(bCryptPasswordEncoder.encode("administrador"));
        user.setRole(role);

        iUserRepository.save(user);

        return "usuario registrado";
    }
}