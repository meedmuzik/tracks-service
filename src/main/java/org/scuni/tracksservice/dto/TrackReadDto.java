package org.scuni.tracksservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class TrackReadDto {
    private Long trackId;
    private String title;
    private String imageUrl;
    private LocalDate releaseDate;
    private Integer albumId;
    private String albumTitle;
}
