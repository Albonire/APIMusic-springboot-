package com.example.MusicApi.service.impl;

import com.example.MusicApi.model.Artist;
import com.example.MusicApi.repository.ArtistRepository;
import com.example.MusicApi.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.MusicApi.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistRepository.findByIsDeletedFalse();
    }

    @Override
    public Artist getArtistById(Long id) throws ResourceNotFoundException {
        return artistRepository.findByIdArtistAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("El artista que buscas no existe."));
    }

    @Override
    public Artist createArtist(Artist artist) {
        artist.setDeleted(false);
        return artistRepository.save(artist);
    }

    @Override
    public Artist updateArtist(Long id, Artist artist) {
        Artist existingArtist = artistRepository.findByIdArtistAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("El artista con el id " + id + " no existe."));

        existingArtist.setName(artist.getName());
        existingArtist.setNationality(artist.getNationality());

        return artistRepository.save(existingArtist);
    }

    @Override
    public void deleteArtist(Long id) {
        Artist artistToDelete = artistRepository.findByIdArtistAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("El artista con el id " + id + " no existe."));

        artistToDelete.setDeleted(true);
        artistRepository.save(artistToDelete);
    }

    @Override
    public void hardDeleteArtist(Long id) {
        Artist artistToDelete = artistRepository.findByIdArtistAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("El artista con el id " + id + " no existe."));
        
        artistRepository.delete(artistToDelete);
    }
} 