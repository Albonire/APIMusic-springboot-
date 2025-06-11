package com.example.MusicApi.controller;

import com.example.MusicApi.model.Artist;
import com.example.MusicApi.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import java.net.URI;
import java.time.Instant;
import com.example.MusicApi.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/artists")
@CrossOrigin(origins = "*")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity.ok(artistService.getAllArtists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id) {
        Artist artist = artistService.getArtistById(id);
        return ResponseEntity.ok(artist);
    }

    @PostMapping
    public ResponseEntity<?> createArtist(@Valid @RequestBody Artist artist, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            }
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "La solicitud contiene errores de validación."
            );
            problemDetail.setTitle("Error de Validación");
            problemDetail.setType(URI.create("http://localhost:8080/errors/validation"));
            problemDetail.setProperty("timestamp", Instant.now());
            problemDetail.setProperty("fieldErrors", fieldErrors);
            return ResponseEntity.of(problemDetail).build();
        }
        return new ResponseEntity<>(artistService.createArtist(artist), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtist(@PathVariable Long id, @RequestBody Artist artist) {
        try {
            return ResponseEntity.ok(artistService.updateArtist(id, artist));
        } catch (ResourceNotFoundException e) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND,
                    "El artista que intentas actualizar no existe."
            );
            problemDetail.setTitle("Recurso no encontrado");
            problemDetail.setType(URI.create("http://localhost:8080/errors/not-found"));
            problemDetail.setProperty("timestamp", Instant.now());
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProblemDetail> deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.OK,
                "El artista ha sido marcado como eliminado exitosamente."
        );
        problemDetail.setTitle("Eliminación Exitosa");
        problemDetail.setType(URI.create("http://localhost:8080/errors/success"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("message", "El artista ha sido marcado como eliminado (soft delete). Puede ser restaurado si es necesario.");

        return ResponseEntity.ok(problemDetail);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ProblemDetail> hardDeleteArtist(@PathVariable Long id) {
        artistService.hardDeleteArtist(id);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.OK,
                "El artista ha sido eliminado permanentemente de la base de datos."
        );
        problemDetail.setTitle("Eliminación Permanente Exitosa");
        problemDetail.setType(URI.create("http://localhost:8080/errors/success"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("message", "El artista ha sido eliminado permanentemente (hard delete). Esta acción no se puede deshacer.");

        return ResponseEntity.ok(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Ya existe un artista con este nombre. Por favor, use un nombre diferente."
        );
        problemDetail.setTitle("Conflicto de datos");
        problemDetail.setType(URI.create("http://localhost:8080/errors/duplicate-resource"));
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "El artista que buscas no existe."
        );
        problemDetail.setTitle("Recurso no encontrado");
        problemDetail.setType(URI.create("http://localhost:8080/errors/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ha ocurrido un error en el servidor. Por favor, inténtalo de nuevo más tarde."
        );
        problemDetail.setTitle("Error de Servidor");
        problemDetail.setType(URI.create("http://localhost:8080/errors/server-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.of(problemDetail).build();
    }
} 