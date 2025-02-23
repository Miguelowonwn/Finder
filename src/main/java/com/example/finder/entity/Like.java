package com.example.finder.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "tabla_likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_usuario_likeado", nullable = false)
    @JsonBackReference
    private Usuario usuarioLikeado;

    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name = "es_like", nullable = false)
    private boolean es_like;

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioLikeado() {
        return usuarioLikeado;
    }

    public void setUsuarioLikeado(Usuario usuarioLikeado) {
        this.usuarioLikeado = usuarioLikeado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuarioDestino() {
        return usuarioLikeado;
    }

    public boolean comprobarEs_like() {
        return es_like;
    }

    public void setEs_like(boolean es_like) {
        this.es_like = es_like;
    }

}
