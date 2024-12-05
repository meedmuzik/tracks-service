package org.scuni.tracksservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.scuni.tracksservice.dto.QueryDto;
import org.scuni.tracksservice.dto.TrackCreateEditDto;
import org.scuni.tracksservice.dto.TrackReadDto;
import org.scuni.tracksservice.mapper.TrackCreateEditMapper;
import org.scuni.tracksservice.mapper.TrackReadMapper;
import org.scuni.tracksservice.model.entity.Track;
import org.scuni.tracksservice.repository.AlbumRepository;
import org.scuni.tracksservice.repository.TrackRepository;
import org.scuni.tracksservice.service.client.CommentsClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final TrackReadMapper trackReadMapper;
    private final TrackCreateEditMapper trackCreateEditMapper;
    private final CommentsClient commentsClient;

    public TrackReadDto getTrackById(Long id) {
        return trackRepository.findById(id)
                .map(trackReadMapper::map)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<TrackReadDto> getTracksByIds(List<Long> ids) {
        List<Track> allByIds = trackRepository.findAllByIds(ids);
        return allByIds.stream().map(trackReadMapper::map).toList();
    }

    public void deleteTrackById(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        trackRepository.deleteTrackRelationships(id);
        trackRepository.delete(track);
    }

    public TrackReadDto updateTrackById(Long id, TrackCreateEditDto trackCreateEditDto) {
        Track track = trackRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        track.setTitle(trackCreateEditDto.getTitle());
        Long trackId = track.getId();
        if (trackCreateEditDto.getAlbumId() != null) {
            Long newAlbumId = trackCreateEditDto.getAlbumId();
            if (track.getAlbumId() != null && !track.getAlbumId().equals(newAlbumId)) {
                albumRepository.deleteTrackAndRelationship(track.getAlbumId(), track.getId());
            }
            albumRepository.findById(trackCreateEditDto.getAlbumId())
                    .ifPresent(album -> albumRepository.addTrackToAlbum(album.getId(), trackId));
        }
        track.setReleaseDate(trackCreateEditDto.getReleaseDate());
        return trackReadMapper.map(track);
    }

    public Long createTrack(TrackCreateEditDto trackCreateEditDto) {
        Track track = trackCreateEditMapper.map(trackCreateEditDto);
        track = trackRepository.save(track);
        Long trackId = track.getId();
        if (trackCreateEditDto.getAlbumId() != null) {
            albumRepository.findById(trackCreateEditDto.getAlbumId())
                    .ifPresent(album -> albumRepository.addTrackToAlbum(album.getId(), trackId));
//            album.ifPresent(track::setAlbum);
        }
        return track.getId();
    }

    public void updateImageIdById(String imageId, Long id) {
        trackRepository.updateImageIdById(id, imageId);
    }

    public Page<TrackReadDto> getTrackByTitle(String title, Pageable pageable) {
        Page<Track> tracks = trackRepository.findTrackByTitleContainingIgnoreCase(title, pageable);
        return tracks.map(trackReadMapper::map);
    }

    public boolean isTrackExistsById(Long id) {
        return trackRepository.existsById(id);
    }

    public void updateTrackRating(Long id) {
        Track track = trackRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Double updatedRating = trackRepository.calculateTrackRating(id);
        if (updatedRating == null) {
            updatedRating = 0.0;
        }
        track.setRating(updatedRating);
        trackRepository.save(track);
    }

    public List<TrackReadDto> getRecommendedTracks(QueryDto query) {
        Map<String, List<Long>> recommendedCommentsIds = commentsClient.getRecommendedCommentsIds(query);
        return trackRepository.findTracksByCommentsIds(recommendedCommentsIds.get("commentsIds"))
                .stream()
                .distinct()
                .map(trackReadMapper::map)
                .toList();
    }

    public Page<TrackReadDto> getRecommendedFeats(Pageable pageable){
        Page<Track> tracks = trackRepository.findBestFeats(pageable);
        return tracks.map(trackReadMapper::map);
    }

}
