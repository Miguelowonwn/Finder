package com.example.finder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // El usuario que inicia el match
    @ManyToOne
    @JoinColumn(name = "match_dado", nullable = false)
    private Usuario matchDado;

    // El usuario que recibe el match
    @ManyToOne
    @JoinColumn(name = "match_recibido", nullable = false)
    private Usuario matchRecibido;

    // Getters y setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getMatchDado() {
        return matchDado;
    }

    public void setMatchDado(Usuario matchDado) {
        this.matchDado = matchDado;
    }

    public Usuario getMatchRecibido() {
        return matchRecibido;
    }

    public void setMatchRecibido(Usuario matchRecibido) {
        this.matchRecibido = matchRecibido;
    }
}