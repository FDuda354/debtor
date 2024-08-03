CREATE TABLE NOTIFICATIONS
(
    id          BIGSERIAL PRIMARY KEY,
    message     TEXT        NOT NULL,
    date        TIMESTAMP   NOT NULL,
    status      VARCHAR(20) NOT NULL,
    customer_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers (id)
);
