ALTER TABLE users ADD COLUMN is_active BOOLEAN DEFAULT FALSE;

ALTER TABLE activation_tokens DROP COLUMN is_active;
