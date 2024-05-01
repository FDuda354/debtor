CREATE TABLE FILES
(
    file_name      VARCHAR PRIMARY KEY,
    content        OID,
    type           VARCHAR,
    transaction_id BIGINT,
    FOREIGN KEY (transaction_id) REFERENCES transactions (id)
);