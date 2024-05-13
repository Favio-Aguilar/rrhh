package com.rrhh.service;

import com.rrhh.Entity.Usuario;
import com.rrhh.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDetailServiceImpl implements UserDetailsService{
    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByCorreoUsuario(username);

        if(user == null) {
            throw new UsernameNotFoundException("Usuario y/o contraseña incorrecta");
        }

        return new MyUserDetails(user);
    }
}
