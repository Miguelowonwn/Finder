package com.example.finder.service;

import com.example.finder.entity.Like;
import com.example.finder.entity.Match;
import com.example.finder.entity.Usuario;
import com.example.finder.repository.LikeRepository;
import com.example.finder.repository.MatchRepository;
import com.example.finder.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    // Crear un nuevo like
    public Like darLike(Usuario usuario, Usuario usuarioLikeado) {
        // Verificar si el like ya existe
        Optional<Like> likeExistente = likeRepository.findByUsuarioAndUsuarioLikeado(usuario, usuarioLikeado);
        if (likeExistente.isPresent()) {
            throw new IllegalStateException("Ya has dado like a este usuario.");
        } else if (usuario.equals(usuarioLikeado)) {
            throw new IllegalStateException("No puedes darte like a ti mismo.");
        }


        // Crear y guardar un nuevo like
        Like like = new Like();
        like.setUsuario(usuario);
        like.setUsuarioLikeado(usuarioLikeado);
        like.setEs_like(true); // Asegura que sea un “like” real

        return likeRepository.save(like);
    }

    // Dar dislike
    public Like darDislike(Usuario usuario, Usuario usuarioLikeado) {
        // Verificar si ya existe un registro (sea like o dislike)
        Optional<Like> likeExistente = likeRepository.findByUsuarioAndUsuarioLikeado(usuario, usuarioLikeado);
        if (likeExistente.isPresent()) {
            throw new IllegalStateException("Ya has registrado un like o dislike a este usuario.");
        } else if (usuario.equals(usuarioLikeado)) {
            throw new IllegalStateException("No puedes darte dislike a ti mismo.");
        }

        // Crear y guardar un nuevo registro con es_like = false
        Like dislike = new Like();
        dislike.setUsuario(usuario);
        dislike.setUsuarioLikeado(usuarioLikeado);
        dislike.setEs_like(false); // Marca el campo con false, indicando "dislike"

        return likeRepository.save(dislike);
    }

    // Obtener la lista de usuarios a los que un usuario ha dado like
    public List<Like> obtenerLikesDados(Usuario usuario) {
        return likeRepository.findByUsuario(usuario);
    }

    // Obtener la lista de usuarios que han dado like a un usuario
    public List<Like> obtenerLikesRecibidos(Usuario usuario) {
        return likeRepository.findByUsuarioLikeado(usuario);
    }

    // Obtener usuarios no likeados por un usuario
    public List<Usuario> obtenerUsuariosNoLikeados(Usuario usuario) {
        // Obtener todos los IDs de los usuarios a los que ya se les dio like/dislike
        List<Integer> usuariosLikeadosIds = likeRepository
                .findByUsuario(usuario)
                .stream()
                .map(like -> like.getUsuarioDestino().getId())
                .toList();

        // Buscar todos los usuarios excepto los que están en la lista de IDs de likeados
        return usuarioRepository.findAllByIdNotIn(usuariosLikeadosIds);
    }


}
