package org.scuni.tracksservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AlbumCreateEditDto {

    @NotBlank(message = "Track title should be not blank")
    @Size(min = 1, max = 32)
    private String title;

    @NotNull
    private LocalDate releaseDate;

}
