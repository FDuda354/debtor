CREATE SEQUENCE debt_id_sequence INCREMENT BY 1 START WITH 1;

CREATE TABLE DEBTS
(
    id             BIGINT PRIMARY KEY DEFAULT nextval('debt_id_sequence'),
    debtor_id      BIGINT NOT NULL,
    creditor_id    BIGINT NOT NULL,
    amount         NUMERIC,
    description    TEXT,
    repayment_date TIMESTAMP,
    start_date     TIMESTAMP,
    status         TEXT   NOT NULL,
    FOREIGN KEY (debtor_id) REFERENCES customers (id),
    FOREIGN KEY (creditor_id) REFERENCES customers (id)
);
