CREATE TABLE author (
    id                  SERIAL          PRIMARY KEY,
    name                VARCHAR(100)    NOT NULL,
    datetime_of_create  TIMESTAMP       NOT NULL
);
ALTER TABLE budget ADD COLUMN author_id INT REFERENCES author;