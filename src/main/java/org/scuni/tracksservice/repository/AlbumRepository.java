package org.scuni.tracksservice.repository;

import org.scuni.tracksservice.model.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AlbumRepository extends JpaRepository<Album, Integer> {

    @Modifying
    @Query("UPDATE Album a SET a.imageId = :imageId WHERE a.id = :id")
    void updateImageIdById(Integer id, String imageId);

    @EntityGraph(attributePaths = {"tracks"})
    Page<Album> findAlbumByTitleContainingIgnoreCase(String title, Pageable pageable);

    @EntityGraph(attributePaths = "tracks")
    @Query("SELECT a FROM Album a WHERE a.id IN :ids")
    List<Album> findAllByIds(List<Integer> ids);

    @Query("SELECT a FROM Album a JOIN FETCH a.tracks WHERE a.id = :id")
    Optional<Album> findByIdWithTracks(Integer id);
}
