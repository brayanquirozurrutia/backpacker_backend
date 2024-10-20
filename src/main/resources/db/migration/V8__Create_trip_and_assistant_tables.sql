CREATE TABLE IF NOT EXISTS trip (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    destination VARCHAR(255) NOT NULL,
    latitude_requested DOUBLE PRECISION,
    longitude_requested DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'REQUESTED'
    );

CREATE TABLE IF NOT EXISTS assistant (
    id SERIAL PRIMARY KEY,
    trip_id INT NOT NULL REFERENCES trip(id),
    user_id INT NOT NULL REFERENCES users(id),
    UNIQUE (trip_id, user_id)
    );
