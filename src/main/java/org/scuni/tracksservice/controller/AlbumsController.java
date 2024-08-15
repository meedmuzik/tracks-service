package org.scuni.tracksservice.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.scuni.tracksservice.dto.AlbumCreateEditDto;
import org.scuni.tracksservice.dto.AlbumReadDto;
import org.scuni.tracksservice.service.AlbumsService;
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
public class AlbumsController {

    private final AlbumsService albumsService;

    @GetMapping("/album/{id}")
    public ResponseEntity<AlbumReadDto> getAlbum(@PathVariable("id") @Min(1) Integer id) {
        return ResponseEntity.ok(albumsService.getAlbumById(id));
    }

    @GetMapping("/albums")
    public ResponseEntity<Object> getAlbumsByIds(@RequestParam("ids") List<Integer> ids) {
        List<AlbumReadDto> albumReadDtos = albumsService.getAlbumsByIds(ids);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("albums", albumReadDtos));
    }

    @DeleteMapping("/album/{id}")
    public ResponseEntity<Object> deleteTrackById(@PathVariable("id") @Min(1) Integer id) {
        albumsService.deleteAlbumById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/album/{id}")
    public ResponseEntity<Object> updateTrackById(@PathVariable("id") @Min(1) Integer id,
                                                  @RequestBody @Validated AlbumCreateEditDto albumCreateEditDto){
        AlbumReadDto album = albumsService.updateAlbumById(id, albumCreateEditDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "album was successfully updated",
                        "album", album));

    }

    @DeleteMapping("/album/{albumId}/track/{trackId}")
    public ResponseEntity<Object> deleteTrackFromAlbum(@PathVariable("albumId") @Min(1) Integer albumId,
                                                       @PathVariable("trackId") @Min(1) Long trackId){
        albumsService.deleteTrackFromAlbum(trackId, albumId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/album")
    public ResponseEntity<Object> createAlbum(@RequestBody @Validated AlbumCreateEditDto albumCreateEditDto) {
        Integer albumId = albumsService.createAlbum(albumCreateEditDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message","Album was uploaded successfully",
                        "id", albumId,
                        "image upload url", "/api/v1/images/album/"+albumId));
    }

    @PatchMapping("/album/{albumId}/track/{trackId}")
    public ResponseEntity<Object> addTrackToAlbum(@PathVariable("albumId") @Min(1) Integer albumId,
                                                  @PathVariable("trackId") @Min(1) Long trackId){
        albumsService.addTrackToAlbum(albumId, trackId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "track was successfully added to album"));
    }

    @GetMapping("/albums/paginated")
    public ResponseEntity<Object> getAlbumsPaginatedByTitle(@RequestParam String title,
                                                     @RequestParam @Min(0) int page,
                                                     @RequestParam @Min(1) int size,
                                                     @RequestParam(required = false) String sort){
        PageRequest pageRequest;

        if (sort == null){
            pageRequest = PageRequest.of(page, size);
        }
        else if (sort.equalsIgnoreCase("asc")) {
            pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("title")));
        } else if (sort.equalsIgnoreCase("desc")){
            pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("title")));
        } else {
            pageRequest = PageRequest.of(page, size);
        }
        Page<AlbumReadDto> albums = albumsService.getAlbumsByTitle(title, pageRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of( "albums", albums));
    }

}
