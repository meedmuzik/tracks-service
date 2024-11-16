package org.scuni.tracksservice.mapper;

import org.scuni.tracksservice.dto.TrackCreateEditDto;
import org.scuni.tracksservice.model.entity.Track;
import org.springframework.stereotype.Component;

@Component
public class TrackCreateEditMapper implements Mapper<TrackCreateEditDto, Track> {

    @Override
    public Track map(TrackCreateEditDto trackCreateEditDto) {
        return Track.builder()
                .title(trackCreateEditDto.getTitle())
                .rating(0.0)
                .albumId(trackCreateEditDto.getAlbumId())
                .releaseDate(trackCreateEditDto.getReleaseDate())
                .build();
    }
}
