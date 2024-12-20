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


import java.time.LocalDate;

@Node("Track")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Track {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String imageId;

    private LocalDate releaseDate;

    private Double rating;

    private Long albumId;

}