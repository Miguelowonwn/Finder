package com.example.finder.service;

import com.example.finder.entity.Like;
import com.example.finder.entity.Match;
import com.example.finder.entity.Usuario;
import com.example.finder.repository.LikeRepository;
import com.example.finder.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MatchRepository matchRepository;

    /**
     * Verifica los posibles matches para un usuario.
     * Revisa todos los likes que el usuario ha dado (que sean likes reales)
     * y si el otro usuario ya te dio like, crea un match (si no existe ya).
     */

    public int verificarMatches(Usuario usuario) {
        int nuevosMatches = 0;

        List<Like> likesDados = likeRepository.findByUsuario(usuario)
                .stream()
                .filter(Like::comprobarEs_like)
                .toList();

        for (Like like : likesDados) {
            Usuario otro = like.getUsuarioLikeado();

            Optional<Like> likeReciproco = likeRepository.findByUsuarioAndUsuarioLikeado(otro, usuario)
                    .filter(Like::comprobarEs_like);

            if (likeReciproco.isPresent()) {
                // ✅ VERIFICAMOS SI YA EXISTE UN MATCH ANTES DE CONTARLO
                boolean matchYaExiste = matchRepository.countMatchesBetweenUsers(usuario, otro) > 0;

                if (!matchYaExiste) {
                    Match match = new Match();
                    if (usuario.getId() < otro.getId()) {
                        match.setMatchDado(usuario);
                        match.setMatchRecibido(otro);
                    } else {
                        match.setMatchDado(otro);
                        match.setMatchRecibido(usuario);
                    }
                    matchRepository.save(match);
                    nuevosMatches++;

                    // 🔥 DEPURACIÓN: Solo imprimimos cuando se crea un match nuevo
                    System.out.println("Nuevo match creado entre: " + usuario.getId() + " y " + otro.getId());
                }
            }
        }

        // 🔥 DEPURACIÓN: Asegurarnos de que `nuevosMatches` es correcto
        System.out.println("Total de nuevos matches detectados: " + nuevosMatches);

        return nuevosMatches;
    }


}