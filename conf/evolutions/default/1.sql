-- !Ups
CREATE TABLE url_mapping_v0(
    id BIGSERIAL,
    short_url TEXT UNIQUE NOT NULL,
    long_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX short_url_idx ON url_mapping_v0(short_url);

-- !Downs

DROP INDEX short_url_idx;
DROP TABLE url_mapping_v0;