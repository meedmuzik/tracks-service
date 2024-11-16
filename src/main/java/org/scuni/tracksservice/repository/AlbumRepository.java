package org.scuni.tracksservice.repository;

import org.scuni.tracksservice.model.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AlbumRepository extends Neo4jRepository<Album, Long> {

    @Query("MATCH (a:Album) WHERE id(a) = $id SET a.imageId = $imageId")
    void updateImageIdById(@Param("id") Long id, @Param("imageId") String imageId);

    @Query(value = "MATCH (a:Album) WHERE a.title CONTAINS $title RETURN a SKIP $skip LIMIT $limit",
            countQuery = "MATCH (a:Album) WHERE a.title CONTAINS $title RETURN count(a)")
    Page<Album> findAlbumByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    @Query("""
                MATCH (a:Album)-[:HAS_TRACK]->(t:Track)
                WHERE id(t) IN ids
                RETURN DISTINCT a
            """)
    List<Album> findAllByIds(@Param("ids") List<Long> ids);

    @Query("""
        MATCH (a:Album)-[r:HAS_TRACK]->(t:Track)
        WHERE id(a) = $albumId
        SET t.albumId = null
        DELETE r, a
        """)
    void deleteAlbumAndRelationships(@Param("albumId") Long albumId);

    @Query("""
                MATCH (a:Album)-[r:HAS_TRACK]->(a:Track)
                WHERE id(a) = $albumId AND id(t) = $trackId
                DELETE r, t
            """)
    void deleteTrackAndRelationship(@Param("albumId") Long albumId, @Param("trackId") Long trackId);

    @Query("""
                MATCH (a:Album), (t:Track)
                WHERE id(a) = $albumId AND id(t) = $trackId
                MERGE (a)-[:HAS_TRACK]->(t)
            """)
    void addTrackToAlbum(@Param("albumId") Long albumId, @Param("trackId") Long trackId);

}
