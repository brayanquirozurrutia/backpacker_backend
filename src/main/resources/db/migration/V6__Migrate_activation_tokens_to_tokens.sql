INSERT INTO tokens (user_id, token, expiration_date, token_type)
SELECT user_id, token, expiration_date, 'ACTIVATION' AS token_type
FROM activation_tokens;
