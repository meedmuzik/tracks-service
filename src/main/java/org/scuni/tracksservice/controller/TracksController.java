package org.scuni.tracksservice.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.scuni.tracksservice.dto.QueryDto;
import org.scuni.tracksservice.dto.TrackCreateEditDto;
import org.scuni.tracksservice.dto.TrackReadDto;
import org.scuni.tracksservice.service.TrackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class TracksController {

    private final TrackService trackService;

    @PostMapping("/recommendations/tracks")
    public ResponseEntity<Object> getRecommendedArtists(@RequestBody QueryDto query) {
        List<TrackReadDto> recommendedTracks = trackService.getRecommendedTracks(query);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("tracks", recommendedTracks));
    }

    @GetMapping("/track/{id}")
    public ResponseEntity<TrackReadDto> getTrack(@PathVariable("id") @Min(1) Long id) {
        return ResponseEntity.ok(trackService.getTrackById(id));
    }

    @GetMapping("/tracks")
    public ResponseEntity<Object> getTracksByIds(@RequestParam("ids") List<Long> ids) {
        List<TrackReadDto> tracksReadDtos = trackService.getTracksByIds(ids);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("tracks", tracksReadDtos));
    }

    @DeleteMapping("/track/{id}")
    public ResponseEntity<Object> deleteTrackById(@PathVariable("id") @Min(1) Long id) {
        trackService.deleteTrackById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/track")
    public ResponseEntity<Object> createTrack(@RequestBody @Validated TrackCreateEditDto trackCreateEditDto) {
        Long trackId = trackService.createTrack(trackCreateEditDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "Track was uploaded successfully",
                        "id", trackId,
                        "image upload url", "/api/v1/images/track/" + trackId));
    }

    @PostMapping("/tracks")
    public ResponseEntity<Object> createTracks(@RequestBody @Validated List<TrackCreateEditDto> trackCreateEditDtos){
        List<Long> ids = trackService.createTracks(trackCreateEditDtos);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "Tracks was uploaded successfully",
                        "ids", ids));
    }

    @PutMapping("/track/{id}")
    public ResponseEntity<Object> updateTrackById(@PathVariable("id") @Min(1) Long id,
                                                  @RequestBody @Validated TrackCreateEditDto trackCreateEditDto) {
        TrackReadDto track = trackService.updateTrackById(id, trackCreateEditDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "album was successfully updated",
                        "track", track));

    }

    @GetMapping("/tracks/paginated")
    public ResponseEntity<Object> getTracksPaginatedByTitle(@RequestParam String title,
                                                            @RequestParam @Min(0) int page,
                                                            @RequestParam @Min(1) int size,
                                                            @RequestParam(required = false) String sort) {
        PageRequest pageRequest;

        if (sort == null) {
            pageRequest = PageRequest.of(page, size);
        } else if (sort.equalsIgnoreCase("asc")) {
            pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("title")));
        } else if (sort.equalsIgnoreCase("desc")) {
            pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("title")));
        } else {
            pageRequest = PageRequest.of(page, size);
        }
        Page<TrackReadDto> tracks = trackService.getTrackByTitle(title, pageRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("tracks", tracks));
    }

    @PostMapping("/track/rating/{id}")
    public ResponseEntity<Object> updateTrackRating(@PathVariable("id") @Min(1) Long id) {
        trackService.updateTrackRating(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tracks/top-rated")
    public ResponseEntity<Object> getTopRatedTracks(@RequestParam @Min(0) int page,
                                                    @RequestParam @Min(1) int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("rating")));
        Page<TrackReadDto> tracks = trackService.getTopRatedTracks(pageRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("tracks", tracks));
    }

}
