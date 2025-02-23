package com.example.finder.controller;

import com.example.finder.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.finder.service.UserService;
import com.example.finder.entity.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager; // Inyección del AuthenticationManager

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestParam("nombre") String nombre,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(name = "foto", required = false) MultipartFile foto
    ) {
        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setPassword(password);

            // Si suben una foto, guardarla
            if (foto != null && !foto.isEmpty()) {
                nuevoUsuario.setFotoPerfil(foto.getBytes());
            }

            // Guarda el usuario (aquí deberías encriptar la contraseña si no lo haces en otro lado)
            userService.registerUser(nuevoUsuario);

            return ResponseEntity.ok("Usuario registrado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }
        String username = authentication.getName();
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
        return ResponseEntity.ok(usuario);
    }


    // Endpoint para actualizar el perfil (nombre y foto)
    @PostMapping(value = "/ajustes", consumes = {"multipart/form-data"})
    public ResponseEntity<?> actualizarPerfil(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(name = "foto", required = false) MultipartFile foto,
            Authentication authentication) {
        try {
            // Obtener el usuario logueado usando el méthodo de UserService
            int userId = userService.obtenerIdUsuarioLogueado();
            Usuario usuario = userService.findById(userId);

            // Si se ha enviado un nuevo nombre (no vacío), actualízalo
            if (nombre != null && !nombre.trim().isEmpty()) {
                usuario.setNombre(nombre);
            }

            // Si se envía una foto, actualizarla
            if (foto != null && !foto.isEmpty()) {
                usuario.setFotoPerfil(foto.getBytes());
            }

            // Guardar los cambios
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Perfil actualizado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el perfil: " + e.getMessage());
        }
    }



}