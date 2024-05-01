CREATE SEQUENCE customer_id_sequence INCREMENT 1;

CREATE TABLE CUSTOMERS
(
    id                 BIGINT DEFAULT nextval('customer_id_sequence') PRIMARY KEY,
    first_name         TEXT    NOT NULL,
    surname            TEXT    NOT NULL,
    email              TEXT    NOT NULL UNIQUE,
    password           TEXT    NOT NULL,
    age                INT     NOT NULL,
    role               TEXT    NOT NULL,
    gender             TEXT    NOT NULL,
    enabled            BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL,
    profile_image      VARCHAR,
    FOREIGN KEY (profile_image) REFERENCES IMAGES (file_name)
);