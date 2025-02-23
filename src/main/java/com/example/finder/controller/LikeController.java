package com.example.finder.controller;

import com.example.finder.entity.Like;
import com.example.finder.entity.Usuario;
import com.example.finder.repository.UsuarioRepository;
import com.example.finder.service.LikeService;
import com.example.finder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finder/")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private UsuarioRepository usuarioRepository; // Inyección del repositorio de Usuario


    // Dar like a un usuario
    @PostMapping("like/{usuarioId}/{usuarioLikeadoId}")
    public Like darLike(
            @PathVariable Integer usuarioId,
            @PathVariable Integer usuarioLikeadoId
    ) {
        // Buscar el usuario que está dando el like
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        // Buscar el usuario al que se le va a dar like
        Usuario usuarioLikeado = usuarioRepository.findById(usuarioLikeadoId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioLikeadoId));

        // Llamar al servicio para crear el like
        return likeService.darLike(usuario, usuarioLikeado);
    }


    // Dar dislike a un usuario
    @PostMapping("dislike/{usuarioId}/{usuarioLikeadoId}")
    public Like darDislike(
            @PathVariable Integer usuarioId,
            @PathVariable Integer usuarioLikeadoId
    ) {
        // Buscar el usuario que está dando el dislike
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        // Buscar el usuario al que se le va a dar dislike
        Usuario usuarioLikeado = usuarioRepository.findById(usuarioLikeadoId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioLikeadoId));

        // Llamar al servicio para crear el dislike
        return likeService.darDislike(usuario, usuarioLikeado);
    }

    // Obtener la lista de likes dados por un usuario
    @GetMapping("/dados/{usuarioId}")
    public List<Like> obtenerLikesDados(@PathVariable Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
        return likeService.obtenerLikesDados(usuario);
    }

    // Obtener la lista de usuarios que no han sido likeados por un usuario
    @GetMapping("/pendientes/{usuarioId}")
    public List<Usuario> obtenerUsuariosNoLikeados(@PathVariable Integer usuarioId) {
        // Buscar el usuario que solicita la lista
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        // Obtener la lista de usuarios a los que no se les ha dado like/dislike
        return likeService.obtenerUsuariosNoLikeados(usuario);
    }


    // Obtener la lista de likes recibidos por un usuario
    @GetMapping("/recibidos/{usuarioId}")
    public List<Like> obtenerLikesRecibidos(@PathVariable Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
        return likeService.obtenerLikesRecibidos(usuario);
    }

}
