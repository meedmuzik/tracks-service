package org.scuni.tracksservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ImageDownload {

    private String extension;
    private byte[] bytes;
}
