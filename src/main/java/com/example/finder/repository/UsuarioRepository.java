package com.example.finder.repository;

import com.example.finder.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

    List<Usuario> findAllByIdNotIn(List<Integer> usuariosLikeadosIds);

    @Query(value = "SELECT * FROM usuarios u WHERE u.id NOT IN (" +
            "SELECT l.id_usuario_likeado FROM tabla_likes l WHERE l.id_usuario = :idUsuario) " +
            "AND u.id NOT IN (" +
            "SELECT l.id_usuario FROM tabla_likes l WHERE l.id_usuario_likeado = :idUsuario AND l.es_like = false) " +
            "AND u.id != :idUsuario",
            nativeQuery = true)
    List<Usuario> obtenerUsuariosPendientes(@Param("idUsuario") int idUsuario);


}
