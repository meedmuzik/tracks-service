package org.scuni.tracksservice.repository;

import org.scuni.tracksservice.model.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackRepository extends Neo4jRepository<Track, Long> {

    @Query("MATCH (t:Track) WHERE id(t) = $id SET t.imageId = $imageId")
    void updateImageIdById(@Param("id") Long id, @Param("imageId") String imageId);

    @Query(value = "MATCH (t:Track) WHERE t.title CONTAINS $title RETURN t SKIP $skip LIMIT $limit",
            countQuery = "MATCH (t:Track) WHERE t.title CONTAINS $title RETURN count(t)")
    Page<Track> findTrackByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    @Query("MATCH (t:Track) WHERE id(t) IN $ids RETURN t")
    List<Track> findAllByIds(@Param("ids") List<Long> ids);

    @Query("""
                MATCH (t:Track)-[r]-()
                WHERE id(t) = $trackId
                DELETE r
            """)
    void deleteTrackRelationships(@Param("trackId") Long trackId);

    @Query("""
                MATCH (c:Comment)-[:COMMENTED_ON]->(t:Track)
                WHERE id(t) = $trackId
                RETURN AVG(c.rating) AS averageRating
            """)
    Double calculateTrackRating(@Param("trackId") Long trackId);

    @Query("MATCH (c:Comment)-[:COMMENTED_ON]->(t:Track) " +
           "WHERE id(c) IN $ids " +
           "RETURN DISTINCT t")
    List<Track> findTracksByCommentsIds(@Param("ids") List<Long> ids);

    @Query(value = """
                MATCH (t:Track)<-[:HAS_TRACK]-(a:Artist)
                WITH t, COUNT(a) AS artistCount
                WHERE artistCount > 1
                RETURN t
                ORDER BY t.rating DESC
            """,
            countQuery = """
                        MATCH (t:Track)<-[:HAS_TRACK]-(a:Artist)
                        WITH t, COUNT(a) AS artistCount
                        WHERE artistCount > 1
                        RETURN COUNT(t)
                    """
    )
    Page<Track> findBestFeats(Pageable pageable);
           
    @Query(value = """
        MATCH (t:Track)<-[:COMMENTED_ON]-(c:Comment)
        WITH t, AVG(c.rating) AS avgRating
        RETURN t
        ORDER BY avgRating DESC
        SKIP $skip LIMIT $limit
        """,
            countQuery = """
        MATCH (t:Track)<-[:COMMENTED_ON]-(c:Comment)
        WITH t, AVG(c.rating) AS avgRating
        RETURN count(t)
        """)
    Page<Track> findTopRatedTracks(Pageable pageable);

    @Query("""
        MATCH (a:Artist)-[:HAS_TRACK]->(t:Track)
        WHERE id(t) = $trackId
        RETURN id(a) AS artistId
        """)
    List<Long> getArtistIdsByTrackId(@Param("trackId") Long trackId);
}

