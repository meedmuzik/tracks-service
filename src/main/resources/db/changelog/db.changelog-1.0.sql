--liquibase formatted sql

--changeset semenovvegor:1
CREATE TABLE album
(
    id           SERIAL PRIMARY KEY,
    image_id     VARCHAR(128),
    release_date DATE        NOT NULL,
    title        VARCHAR(32) NOT NULL
);

--changeset semenovvegor:2
CREATE TABLE track
(
    id           BIGSERIAL PRIMARY KEY,
    image_id     VARCHAR(128),
    title        VARCHAR(32) NOT NULL,
    release_date DATE        NOT NULL,
    album_id     INTEGER REFERENCES album (id)
);
