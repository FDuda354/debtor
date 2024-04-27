CREATE SEQUENCE customer_id_sequence INCREMENT 1;


CREATE TABLE customers (
id BIGINT DEFAULT nextval('customer_id_sequence') PRIMARY KEY,
name TEXT NOT NULL,
email TEXT NOT NULL,
password TEXT NOT NULL,
age INT NOT NULL
);
