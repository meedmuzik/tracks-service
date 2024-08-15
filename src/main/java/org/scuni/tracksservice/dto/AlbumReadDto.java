package org.scuni.tracksservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class AlbumReadDto {

    private String title;
    private LocalDate releaseDate;
    private String imageUrl;
    private List<TrackReadDto> tracks;
}
