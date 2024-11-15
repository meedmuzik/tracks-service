package org.scuni.tracksservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.scuni.tracksservice.dto.AlbumCreateEditDto;
import org.scuni.tracksservice.dto.AlbumReadDto;
import org.scuni.tracksservice.mapper.AlbumCreateEditMapper;
import org.scuni.tracksservice.mapper.AlbumReadMapper;
import org.scuni.tracksservice.model.entity.Album;
import org.scuni.tracksservice.model.entity.Track;
import org.scuni.tracksservice.repository.AlbumRepository;
import org.scuni.tracksservice.repository.TrackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AlbumsService {

    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final AlbumReadMapper albumReadMapper;
    private final AlbumCreateEditMapper albumCreateEditMapper;

    public AlbumReadDto getAlbumById(Long id) {
        return albumRepository.findById(id)
                .map(albumReadMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<AlbumReadDto> getAlbumsByIds(List<Long> ids) {
        List<Album> allByIds = albumRepository.findAllByIds(ids);
        return allByIds.stream().map(albumReadMapper::map).collect(Collectors.toList());
    }

    public void deleteAlbumById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        albumRepository.deleteAlbumAndRelationships(id);
//        albumRepository.delete(album);
    }

    public void deleteTrackFromAlbum(Long trackId, Long albumId) {
        Album album = albumRepository.findById(albumId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "album with id" + albumId + " does not exist"));
        Set<Track> tracks = album.getTracks();
        Track trackToRemove = tracks.stream()
                .filter(track -> track.getId().equals(trackId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "track with id" + trackId + "not in album"));
        albumRepository.deleteTrackAndRelationship(albumId, trackId);
        tracks.remove(trackToRemove);
        trackToRemove.setAlbum(null);
    }

    public AlbumReadDto updateAlbumById(Long id, AlbumCreateEditDto albumCreateEditDto) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        album.setTitle(albumCreateEditDto.getTitle());
        album.setReleaseDate(albumCreateEditDto.getReleaseDate());
        return albumReadMapper.map(album);
    }

    public Long createAlbum(AlbumCreateEditDto albumCreateEditDto) {
        Album album = albumCreateEditMapper.map(albumCreateEditDto);
        albumRepository.save(album);
        return album.getId();
    }

    public void addTrackToAlbum(Long albumId, Long trackId) {
        Optional<Track> optionalTrack = trackRepository.findById(trackId);
        if (optionalTrack.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "track with id: " + trackId + "does not exist");
        }
        Optional<Album> optionalAlbum = albumRepository.findById(albumId);
        if (optionalAlbum.isPresent()) {
//            Album album = optionalAlbum.get();
//            album.addTrack(optionalTrack.get());
//            Track track = optionalTrack.get();
//            track.setAlbum(album);
            albumRepository.addTrackToAlbum(albumId, trackId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "album with id: " + albumId + "does not exist");
        }
    }

    public Page<AlbumReadDto> getAlbumsByTitle(String title, Pageable pageable) {
        Page<Album> albums = albumRepository.findAlbumByTitleContainingIgnoreCase(title, pageable);
        return albums.map(albumReadMapper::map);
    }

    public void updateImageIdById(String imageId, Long id) {
        albumRepository.updateImageIdById(id, imageId);
    }


    public boolean isAlbumExistsById(Long id) {
        return albumRepository.existsById(id);
    }

}
