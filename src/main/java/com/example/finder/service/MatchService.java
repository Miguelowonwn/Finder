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
    public void verificarMatches(Usuario usuario) {
        // Obtén todos los likes que el usuario dio y que sean likes (es decir, es_like = true)
        List<Like> likesDados = likeRepository.findByUsuario(usuario)
                .stream()
                .filter(Like::comprobarEs_like)
                .toList();

        for (Like like : likesDados) {
            Usuario otro = like.getUsuarioLikeado();
            // Comprueba si el otro usuario ya te dio like
            Optional<Like> likeReciproco = likeRepository.findByUsuarioAndUsuarioLikeado(otro, usuario)
                    .filter(Like::comprobarEs_like);

            if (likeReciproco.isPresent()) {
                // Evitamos duplicados comprobando en ambas direcciones
                Optional<Match> matchExistente = matchRepository.findByMatchDadoAndMatchRecibido(usuario, otro);
                Optional<Match> matchExistenteInverso = matchRepository.findByMatchDadoAndMatchRecibido(otro, usuario);
                if (matchExistente.isEmpty() && matchExistenteInverso.isEmpty()) {
                    Match match = new Match();
                    // Para tener un orden consistente, asigna el de menor ID como el que "inicia" el match.
                    if (usuario.getId() < otro.getId()) {
                        match.setMatchDado(usuario);
                        match.setMatchRecibido(otro);
                    } else {
                        match.setMatchDado(otro);
                        match.setMatchRecibido(usuario);
                    }
                    matchRepository.save(match);
                }
            }
        }
    }
}
