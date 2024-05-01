CREATE SEQUENCE transaction_id_sequence INCREMENT BY 1 START WITH 1;

CREATE TABLE TRANSACTIONS
(
    id                        BIGINT PRIMARY KEY DEFAULT nextval('transaction_id_sequence'),
    debt_id                   BIGINT NOT NULL,
    amount                    NUMERIC,
    description               TEXT,
    payment_date              TIMESTAMP,
    FOREIGN KEY (debt_id) REFERENCES debts (id)
);
