CREATE TABLE item (
                      id SERIAL PRIMARY KEY,
                      description TEXT,
                      created TIMESTAMP,
                      done BOOLEAN
);

-- ALTER TABLE post ALTER COLUMN id RESTART WITH 1;
-- DELETE FROM post;