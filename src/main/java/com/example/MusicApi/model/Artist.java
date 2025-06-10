package com.example.MusicApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdArtist")
    private Integer idArtist;

    @Column(name = "Name", nullable = false, length = 100)
    @jakarta.validation.constraints.NotBlank(message = "El nombre es obligatorio")
    @jakarta.validation.constraints.Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String name;

    @Column(name = "Nationality", length = 200)
    @jakarta.validation.constraints.Size(max = 200, message = "La nacionalidad no puede tener más de 200 caracteres")
    private String nationality;

    @Column(name = "IsDeleted")
    private boolean isDeleted = false;
} 