ALTER TABLE customers
ADD CONSTRAINT customer_email_unique UNIQUE (email);