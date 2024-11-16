package org.scuni.tracksservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Node("Album")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Album {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private LocalDate releaseDate;

    private String imageId;

    @Relationship(type = "HAS_TRACK", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private List<Track> tracks = new ArrayList<>();
}


