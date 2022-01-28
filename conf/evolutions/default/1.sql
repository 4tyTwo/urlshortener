-- !Ups
CREATE SEQUENCE url_mapping_v0_idx_seq START WITH 140608; -- 52^3, just so short urls will be at least 4 chars long
CREATE TABLE url_mapping_v0(
    id BIGINT NOT NULL DEFAULT nextval('url_mapping_v0_idx_seq'),
    long_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (long_url)
);

-- !Downs
DROP SEQUENCE url_mapping_v0_idx_seq;
DROP TABLE url_mapping_v0;