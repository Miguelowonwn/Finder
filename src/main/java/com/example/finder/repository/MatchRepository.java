package com.example.finder.repository;

import com.example.finder.entity.Match;
import com.example.finder.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
