package com.rrhh.Repository;

import com.rrhh.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<Usuario, Integer>{
    Usuario findByCorreoUsuario(String correo_usuario);
    
}
