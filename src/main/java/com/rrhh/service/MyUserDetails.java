package com.rrhh.service;

import com.rrhh.Entity.Rol;
import com.rrhh.Entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails{

    private Usuario user;

    public MyUserDetails(Usuario user) {
        this.user = user;
    }

    public String getNombreUsuario() {
        return user.getNombreUsuario();
    }
    
    public int getIdUsuario() {
    	int userId = user.getIdUsuario();
    	return userId;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Rol role = user.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getNombre()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getClaveUsuario();
    }

    @Override
    public String getUsername() {
       return user.getCorreoUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}

