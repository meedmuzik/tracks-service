package org.scuni.tracksservice.repository;

import org.scuni.tracksservice.model.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    @Modifying
    @Query("UPDATE Track t SET t.imageId = :imageId WHERE t.id = :id")
    void updateImageIdById(Long id, String imageId);

    @EntityGraph(attributePaths = {"album"})
    Page<Track> findTrackByTitleContainingIgnoreCase(String title, Pageable pageable);

    @EntityGraph(attributePaths = "album")
    @Query("SELECT t FROM Track t WHERE t.id IN :ids")
    List<Track> findAllByIds(List<Long> ids);
}
