package com.example.MusicApi.service;

import com.example.MusicApi.model.Artist;
import java.util.List;
import java.util.Optional;

public interface ArtistService {
    List<Artist> getAllArtists();
    Optional<Artist> getArtistById(Long id);
    Artist createArtist(Artist artist);
    Artist updateArtist(Long id, Artist artist);
    void deleteArtist(Long id);
    void hardDeleteArtist(Long id);
} 