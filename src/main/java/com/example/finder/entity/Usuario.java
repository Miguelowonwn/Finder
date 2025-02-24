    package com.example.finder.entity;

    import com.fasterxml.jackson.annotation.*;
    import jakarta.persistence.*;
    import org.springframework.security.core.GrantedAuthority;

    import java.time.LocalDateTime;
    import java.util.Collection;
    import java.util.List;

    @Entity
    @Table(name = "usuarios")
    public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(nullable = false, length = 100)
        private String nombre;


        @Column(nullable = false, unique = true, length = 255)
        private String username;

        @JsonIgnore // No se incluirá en la serialización JSON
        @Column(nullable = false)
        private String password;

        @Lob
        private byte[] fotoPerfil;

        @Column(nullable = false, updatable = false)
        private LocalDateTime creadoEn = LocalDateTime.now();

        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<Like> likesDados;

        @JsonIgnore // No se incluirá en la serialización JSON
        @OneToMany(mappedBy = "usuarioLikeado", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonBackReference
        private List<Like> likesRecibidos;

        // Getters y setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getEmail() {
            return username;
        }

        public void setEmail(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public byte[] getFotoPerfil() {
            return fotoPerfil;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setFotoPerfil(byte[] fotoPerfil) {
            this.fotoPerfil = fotoPerfil;
        }

        public LocalDateTime getCreadoEn() {
            return creadoEn;
        }

        public void setCreadoEn(LocalDateTime creadoEn) {
            this.creadoEn = creadoEn;
        }

        public List<Like> getLikesDados() {
            return likesDados;
        }

        public void setLikesDados(List<Like> likesDados) {
            this.likesDados = likesDados;
        }

        public List<Like> getLikesRecibidos() {
            return likesRecibidos;
        }

        public void setLikesRecibidos(List<Like> likesRecibidos) {
            this.likesRecibidos = likesRecibidos;
        }

    }
