package org.scuni.tracksservice.mapper;

import lombok.RequiredArgsConstructor;
import org.scuni.tracksservice.dto.TrackReadDto;
import org.scuni.tracksservice.model.entity.Album;
import org.scuni.tracksservice.model.entity.Track;
import org.scuni.tracksservice.repository.AlbumRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrackReadMapper implements Mapper<Track, TrackReadDto> {

    private final AlbumRepository albumRepository;

    @Override
    public TrackReadDto map(Track track) {
        Optional<Album> album = Optional.empty();
        if (track.getAlbumId() != null){
            album = albumRepository.findById(track.getAlbumId());
        }
        return TrackReadDto.builder()
                .trackId(track.getId())
                .imageUrl(track.getImageId() == null ? null : "/api/v1/images/" + track.getImageId())
                .title(track.getTitle())
                .albumId(album.map(Album::getId).orElse(null))
                .albumTitle(album.map(Album::getTitle).orElse(null))
                .releaseDate(track.getReleaseDate())
                .rating(track.getRating())
                .build();
    }
}
