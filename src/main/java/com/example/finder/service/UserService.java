package com.example.finder.service;

import com.example.finder.entity.Usuario;
import com.example.finder.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registerUser(Usuario nuevoUsuario) {
        if (usuarioRepository.findByUsername(nuevoUsuario.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe.");
        }
        // Encriptar contraseña
        nuevoUsuario.setPassword(passwordEncoder.encode(nuevoUsuario.getPassword()));

        // La fotoPerfil ya viene seteada por el controlador (en caso de que suban archivo)
        return usuarioRepository.save(nuevoUsuario);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles("USER")
                .build();

    }

    public List<Usuario> obtenerUsuariosPendientes(int idUsuario) {
        return usuarioRepository.obtenerUsuariosPendientes(idUsuario);
    }

    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado..."));
    }

    // Métohdo para obtener el ID del usuario logueado
    public int obtenerIdUsuarioLogueado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No hay usuario autenticado");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User userSpring) {
            // userSpring.getUsername() -> tu username
            Usuario usuarioEntity = usuarioRepository.findByUsername(userSpring.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado en BD"));
            return usuarioEntity.getId();
        } else {
            throw new IllegalStateException("El principal no es de tipo UserDetails");
        }
    }

}