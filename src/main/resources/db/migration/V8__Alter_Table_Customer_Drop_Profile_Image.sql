ALTER TABLE CUSTOMERS
DROP CONSTRAINT IF EXISTS customers_profile_image_fkey;

ALTER TABLE CUSTOMERS
ALTER COLUMN profile_image TYPE TEXT;
