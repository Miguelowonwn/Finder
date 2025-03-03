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
import org.springframework.web.multipart.MultipartFile;

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


    @GetMapping("/{id}/foto")
    public ResponseEntity<byte[]> obtenerFotoPerfil(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            byte[] fotoPerfil = usuario.getFotoPerfil();

            if (fotoPerfil == null || fotoPerfil.length == 0) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Cambiar si es PNG
                    .body(fotoPerfil);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/foto")
    public ResponseEntity<String> subirFoto(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            usuario.setFotoPerfil(file.getBytes());
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Foto subida exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la foto");
        }
    }


    @GetMapping("/pendientes")
    public List<Usuario> getUsuariosPendientes() {
        int loggedInUserId = userService.obtenerIdUsuarioLogueado(); // Obtener el ID del usuario logueado
        var lista =  userService.obtenerUsuariosPendientes(loggedInUserId); // Obtener usuarios pendientes
        return lista;
    }

}

