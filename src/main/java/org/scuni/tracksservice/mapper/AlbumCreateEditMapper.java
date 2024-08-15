package org.scuni.tracksservice.mapper;

import org.scuni.tracksservice.dto.AlbumCreateEditDto;
import org.scuni.tracksservice.model.entity.Album;
import org.springframework.stereotype.Component;

@Component
public class AlbumCreateEditMapper implements Mapper<AlbumCreateEditDto, Album>{

    @Override
    public Album map(AlbumCreateEditDto albumCreateEditDto) {
        return Album.builder()
                .title(albumCreateEditDto.getTitle())
                .releaseDate(albumCreateEditDto.getReleaseDate())
                .build();

    }
}
