--liquibase formatted sql

--changeset egorsemenovv:1
CREATE TABLE album
(
    id SERIAL PRIMARY KEY,
    image_id VARCHAR(32),
    title VARCHAR(32) NOT NULL
);

--changeset egorsemenovv:2
CREATE TABLE track
(
    id           BIGSERIAL PRIMARY KEY,
    image_id     VARCHAR(32),
    title        VARCHAR(32) NOT NULL,
    release_date DATE    NOT NULL,
    album_id     INTEGER REFERENCES album(id)
);
