CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL UNIQUE
);