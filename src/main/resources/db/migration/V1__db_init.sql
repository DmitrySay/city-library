CREATE SEQUENCE city_id_seq
    INCREMENT 1
    MINVALUE 0
    MAXVALUE 10000000
    START 1001
    CACHE 1;

CREATE TABLE IF NOT EXISTS city
(
    id    BIGINT NOT NULL DEFAULT nextval('city_id_seq'::regclass) PRIMARY KEY,
    name  VARCHAR(255),
    photo TEXT
);

COPY city FROM '/tmp/cities.csv' DELIMITER ',' CSV HEADER;


