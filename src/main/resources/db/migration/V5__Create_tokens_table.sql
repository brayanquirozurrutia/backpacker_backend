CREATE TABLE tokens (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(6) NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    token_type VARCHAR(20) NOT NULL
);
