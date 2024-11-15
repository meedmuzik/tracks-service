package org.scuni.tracksservice.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.scuni.tracksservice.model.ImageDownload;
import org.scuni.tracksservice.service.AlbumsService;
import org.scuni.tracksservice.service.ImageService;
import org.scuni.tracksservice.service.TrackService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Validated
public class ImageController {

    private final ImageService imageService;
    private final TrackService trackService;
    private final AlbumsService albumsService;

    @PostMapping("/track/{id}")
    public ResponseEntity<Object> uploadImageForTrack(@PathVariable("id") @Min(1) Long id,
                                                      MultipartFile image) {
        if (!trackService.isTrackExistsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "track with id: " + id + "does not exist"));
        }
        String fileName = imageService.upload(image);
        trackService.updateImageIdById(fileName, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "image was successfully updated"));
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable("imageId") String imageId) {
        ImageDownload download = imageService.download(imageId);
        String extension = download.getExtension();
        MediaType mediaType;
        if (extension.equals("png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else {
            mediaType = MediaType.IMAGE_JPEG;
        }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(new ByteArrayResource(download.getBytes()));
    }

    @PostMapping("/album/{id}")
    public ResponseEntity<Object> uploadImageForAlbum(@PathVariable("id") @Min(1) Long id,
                                                      MultipartFile image) {
        if (!albumsService.isAlbumExistsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "album with id: " + id + "does not exist"));
        }
        String fileName = imageService.upload(image);
        albumsService.updateImageIdById(fileName, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "image was successfully updated",
                        "imageId", fileName));
    }
}

