package com.example.MusicApi.repository;

import com.example.MusicApi.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    
    List<Artist> findByIsDeletedFalse();
    
    Optional<Artist> findByIdArtistAndIsDeletedFalse(Long id);
} 