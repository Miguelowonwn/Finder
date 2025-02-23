package com.example.finder.repository;

import com.example.finder.entity.Like;
import com.example.finder.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    // Obtener todos los likes dados por un usuario
    List<Like> findByUsuario(Usuario usuario);

    // Obtener todos los likes recibidos por un usuario
    List<Like> findByUsuarioLikeado(Usuario usuarioLikeado);

    // Verificar si un usuario ya ha dado like a otro usuario
    Optional<Like> findByUsuarioAndUsuarioLikeado(Usuario usuario, Usuario usuarioLikeado);
}
