CREATE TABLE CUSTOMER_FRIENDS
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    friend_id   BIGINT NOT NULL,
    status      TEXT   NOT NULL,
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
            REFERENCES CUSTOMERS (id),
    CONSTRAINT fk_friend
        FOREIGN KEY (friend_id)
            REFERENCES CUSTOMERS (id)
);
