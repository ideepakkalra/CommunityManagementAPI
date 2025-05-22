delete from t_event;
delete from t_login;
delete from t_community_referral;
delete from t_user;
-- For 200 Test
insert into t_user (id, version, phone_number, email, first_name, last_name, description, gender, date_of_birth, referred_by_id, updated_by_id, updated_on, state, type)
    values (0, 0, '+10000000000', 'Test-Admin@eventmanagement.com', 'Test', 'Admin', 'Administrator account for this community.', 0, '1900-01-01', null, null, '2025-01-01', 1, 1);
insert into t_login (phone_number, passcode, status, user_id, retry_count) values ('+10000000000', '000000', 0, 0, 0);
-- For Locking Test
insert into t_user (id, version, phone_number, email, first_name, last_name, description, gender, date_of_birth, referred_by_id, updated_by_id, updated_on, state, type)
    values (1, 0, '+10000000001', 'LoginControllerTest-1@eventmanagement.com', 'Test', 'User1', 'Test User 1.', 0, '1900-01-01', 0, 0, '2025-01-01', 1, 0);
insert into t_login (phone_number, passcode, status, user_id, retry_count) values ('+10000000001', '000000', 0, 1, 0);