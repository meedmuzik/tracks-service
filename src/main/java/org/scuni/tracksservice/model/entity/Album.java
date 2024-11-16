package org.scuni.tracksservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Node("Album")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "tracks")
@EqualsAndHashCode(of = "id")
public class Album {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private LocalDate releaseDate;

    private String imageId;
    @Builder.Default
    @Relationship(type = "HAS_TRACK", direction = Relationship.Direction.OUTGOING)
    private Set<Track> tracks = new HashSet<>();

    public void addTrack(Track track) {
        tracks.add(track);
    }
}


