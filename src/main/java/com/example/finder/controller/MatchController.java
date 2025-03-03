package com.example.finder.controller;

import com.example.finder.entity.Match;
import com.example.finder.entity.Usuario;
import com.example.finder.repository.MatchRepository;
import com.example.finder.service.MatchService;
import com.example.finder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/finder/matches")
public class MatchController {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MatchService matchService;

    @GetMapping
    public ResponseEntity<?> getMatchesForLoggedInUser() {
        try {
            // Obtén el ID del usuario logueado
            int loggedInUserId = userService.obtenerIdUsuarioLogueado();
            // Recupera la entidad Usuario
            Usuario loggedInUser = userService.findById(loggedInUserId);

            // Consulta los matches donde el usuario es el que dio el match
            List<Match> matchesDado = matchRepository.findByMatchDado(loggedInUser);
            // Consulta los matches donde el usuario es el que recibió el match
            List<Match> matchesRecibido = matchRepository.findByMatchRecibido(loggedInUser);

            // Une ambas listas
            List<Match> allMatches = new ArrayList<>();
            allMatches.addAll(matchesDado);
            allMatches.addAll(matchesRecibido);

            return ResponseEntity.ok(allMatches);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener matches: " + e.getMessage());
        }
    }

    // Endpoint para disparar la verificación de matches para el usuario logueado
    @PostMapping("/verificar")
    public ResponseEntity<?> verificarMatches(Authentication authentication) {
        try {
            int userId = userService.obtenerIdUsuarioLogueado();
            Usuario usuario = userService.findById(userId);

            int nuevosMatches = matchService.verificarMatches(usuario);

            // 🔥 DEPURACIÓN: Verifica cuántos nuevos matches detecta realmente
            System.out.println("Matches nuevos detectados: " + nuevosMatches);

            // Si no hay nuevos matches, forzar que la respuesta sea `0`
            return ResponseEntity.ok().body("{\"newMatches\": " + (nuevosMatches > 0 ? nuevosMatches : 0) + "}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Error al verificar matches: " + e.getMessage() + "\"}");
        }
    }


}