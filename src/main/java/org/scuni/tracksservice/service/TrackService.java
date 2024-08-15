package org.scuni.tracksservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.scuni.tracksservice.dto.TrackCreateEditDto;
import org.scuni.tracksservice.dto.TrackReadDto;
import org.scuni.tracksservice.mapper.TrackCreateEditMapper;
import org.scuni.tracksservice.mapper.TrackReadMapper;
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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final TrackReadMapper trackReadMapper;
    private final TrackCreateEditMapper trackCreateEditMapper;

    public TrackReadDto getTrackById(Long id) {
        return trackRepository.findById(id)
                .map(trackReadMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<TrackReadDto> getTracksByIds(List<Long> ids) {
        List<Track> allByIds = trackRepository.findAllByIds(ids);
        return allByIds.stream().map(trackReadMapper::map).collect(Collectors.toList());
    }

    public void deleteTrackById(Long id){
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        trackRepository.delete(track);
    }

    public TrackReadDto updateTrackById(Long id, TrackCreateEditDto trackCreateEditDto) {
        Track track = trackRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        track.setTitle(trackCreateEditDto.getTitle());
        if (trackCreateEditDto.getAlbumId() != null) {
            albumRepository.findById(trackCreateEditDto.getAlbumId()).ifPresent(track::setAlbum);
        }
        track.setReleaseDate(trackCreateEditDto.getReleaseDate());
        return trackReadMapper.map(track);
    }

    public Long createTrack(TrackCreateEditDto trackCreateEditDto) {
        Track track = trackCreateEditMapper.map(trackCreateEditDto);
        if (trackCreateEditDto.getAlbumId() != null) {
            Optional<Album> album = albumRepository.findById(trackCreateEditDto.getAlbumId());
            album.ifPresent(track::setAlbum);
        }
        trackRepository.saveAndFlush(track);
        return track.getId();
    }

    public void updateImageIdById(String imageId, Long id){
        trackRepository.updateImageIdById(id, imageId);
    }

    public Page<TrackReadDto> getTrackByTitle(String title, Pageable pageable) {
        Page<Track> tracks = trackRepository.findTrackByTitleContainingIgnoreCase(title, pageable);
        return tracks.map(trackReadMapper::map);
    }

    public boolean isTrackExistsById(Long id){
        return trackRepository.existsById(id);
    }
}