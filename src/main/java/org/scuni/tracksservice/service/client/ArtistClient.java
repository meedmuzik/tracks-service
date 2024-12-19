package org.scuni.tracksservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient("artist-service")
public interface ArtistClient {

    @PostMapping(value = "api/v1/artist/rating/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateArtistRating(@PathVariable Long id);

}
