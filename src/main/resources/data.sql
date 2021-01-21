INSERT INTO role (role_id, role_name) VALUES (1, 'ADMIN');
INSERT INTO role (role_id, role_name) VALUES (2, 'USER');
INSERT INTO users (user_id, uuid, username, email, password, is_enabled, is_account_non_expired, is_account_non_locked, is_credentials_non_expired) VALUES (0L, 'another uuid', 'Monitor', 'monitor@email.com', 'randomPassword', false, false, false, false);
INSERT INTO code (id, body, date_time, time_in_seconds, uuid, views_left, user_id) VALUES (0L, 'Testing', '2020-01-02 11:15:29', 60L, 'random uuids', 1, 0L);
INSERT INTO user_role (user_id, role_id) VALUES (0, 1);

