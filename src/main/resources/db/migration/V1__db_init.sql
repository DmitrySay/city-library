CREATE SEQUENCE cities_seq
    INCREMENT 50
    MINVALUE 0
    MAXVALUE 10000000
    START 1050;


CREATE TABLE IF NOT EXISTS cities
(
    id    BIGINT NOT NULL DEFAULT nextval('cities_seq'::regclass) PRIMARY KEY,
    name  VARCHAR(255),
    photo TEXT
);

COPY cities FROM '/tmp/cities.csv' DELIMITER ',' CSV HEADER;


