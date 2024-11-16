package org.scuni.tracksservice.mapper;

import lombok.RequiredArgsConstructor;
import org.scuni.tracksservice.dto.AlbumReadDto;
import org.scuni.tracksservice.dto.TrackReadDto;
import org.scuni.tracksservice.model.entity.Album;
import org.scuni.tracksservice.model.entity.Track;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AlbumReadMapper implements Mapper<Album, AlbumReadDto>{

    private final TrackReadMapper trackReadMapper;

    @Override
    public AlbumReadDto map(Album album) {
        List<TrackReadDto> tracks = new ArrayList<>();
        List<Track> albumTracks = album.getTracks();
        for (Track albumTrack : albumTracks) {
            tracks.add(trackReadMapper.map(albumTrack));
        }
        return AlbumReadDto.builder()
                .title(album.getTitle())
                .imageUrl(album.getImageId()==null ? null : "/api/v1/images/album/" + album.getImageId())
                .releaseDate(album.getReleaseDate())
                .tracks(tracks)
                .build();
    }
}
