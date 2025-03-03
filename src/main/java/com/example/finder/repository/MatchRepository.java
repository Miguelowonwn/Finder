package com.example.finder.repository;

import com.example.finder.entity.Match;
import com.example.finder.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    // Méthodo para obtener todos los matches en los que un usuario es el que dio el match
    List<Match> findByMatchDado(Usuario matchDado);

    // Méthodo para obtener todos los matches en los que un usuario es el que recibió el match
    List<Match> findByMatchRecibido(Usuario matchRecibido);

    // Méthodo para buscar un match específico entre dos usuarios
    Optional<Match> findByMatchDadoAndMatchRecibido(Usuario matchDado, Usuario matchRecibido);

    @Query("SELECT COUNT(m) FROM Match m WHERE m.matchRecibido = :usuario OR m.matchDado = :usuario")
    int contarNuevosMatchesParaUsuario(@Param("usuario") Usuario usuario);

    // Méthodo para verificar si ya existe un match en cualquier dirección
    @Query("SELECT COUNT(m) > 0 FROM Match m WHERE (m.matchDado = :usuario1 AND m.matchRecibido = :usuario2) OR (m.matchDado = :usuario2 AND m.matchRecibido = :usuario1)")
    boolean existsMatchBetweenUsers(@Param("usuario1") Usuario usuario1, @Param("usuario2") Usuario usuario2);

    @Query("SELECT COUNT(m) FROM Match m WHERE (m.matchDado = :usuario1 AND m.matchRecibido = :usuario2) OR (m.matchDado = :usuario2 AND m.matchRecibido = :usuario1)")
    int countMatchesBetweenUsers(@Param("usuario1") Usuario usuario1, @Param("usuario2") Usuario usuario2);

}