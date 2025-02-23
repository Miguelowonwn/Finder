package com.example.finder.controller;

import com.example.finder.entity.Usuario;
import com.example.finder.repository.UsuarioRepository;
import com.example.finder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UserService userService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/todos")
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }


    @GetMapping("/usuarios/{id}/foto")
    public ResponseEntity<byte[]> obtenerFotoPerfil(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            byte[] fotoPerfil = usuario.getFotoPerfil();

            if (fotoPerfil == null || fotoPerfil.length == 0) {
                // Si no tiene foto, puedes devolver una imagen por defecto
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg") // O "image/png" según el tipo
                    .body(fotoPerfil);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pendientes")
    public List<Usuario> getUsuariosPendientes() {
        int loggedInUserId = userService.obtenerIdUsuarioLogueado(); // Obtener el ID del usuario logueado
        var lista =  userService.obtenerUsuariosPendientes(loggedInUserId); // Obtener usuarios pendientes
        return lista;
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<Resource> obtenerFotoUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getFotoPerfil() == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(usuario.getFotoPerfil());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Cambia si es PNG o GIF
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"foto.jpg\"")
                .body(resource);
    }

}

