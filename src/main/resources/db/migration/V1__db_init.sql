CREATE TABLE IF NOT EXISTS city
(
    id    BIGSERIAL primary key,
    name  VARCHAR(255),
    photo TEXT
);

COPY city FROM '/tmp/cities.csv' DELIMITER ',' CSV HEADER;


