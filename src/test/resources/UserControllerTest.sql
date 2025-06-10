delete from t_event;
delete from t_login;
delete from t_community_referral;
delete from t_user;
-- Admin user
insert into t_user (id, version, phone_number, email, first_name, last_name, description, gender, date_of_birth, referred_by_id, updated_by_id, updated_on, state, type)
    values (0, 0, '+10000000000', 'Test-Admin@eventmanagement.com', 'Test', 'Admin', 'Administrator account for this community.', 0, '1900-01-01', null, null, '2025-01-01', 1, 1);
insert into t_login (phone_number, passcode, state, user_id, retry_count) values ('+10000000000', '000000', 0, 0, 0);
insert into t_community_referral (id, version, code, referrer_id, phone_number, state, updated_by_id, updated_on)
    values (0, 0, 'e3f2d82b-2859-4d0f-a2e7-5c8bc7fee333', null, '+10000000000', 0, 0, '1900-01-01');
-- Standard user referral for testing new user creation
insert into t_community_referral (id, version, code, referrer_id, phone_number, state, updated_by_id, updated_on)
    values (1, 0, 'e3f2d82b-2859-4d0f-a2e7-5c8bc7fee334', 0, '+10000000001', 0, 0, '1900-01-01');
-- Standard user for testing update user
insert into t_user (id, version, phone_number, email, first_name, last_name, description, gender, date_of_birth, referred_by_id, updated_by_id, updated_on, state, type)
    values (100, 0, '+11111111111', 'Test-User@eventmanagement.com', 'Test', 'User', 'Standard account for this community.', 0, '1900-01-01', 0, 0, '2025-01-01', 1, 1);
insert into t_login (phone_number, passcode, state, user_id, retry_count) values ('+11111111111', '000000', 0, 100, 0);
insert into t_community_referral (id, version, code, referrer_id, phone_number, state, updated_by_id, updated_on)
    values (100, 0, 'e3f2d82b-2859-4d0f-a2e7-5c8bc7fee335', 0, '+11111111111', 0, 0, '1900-01-01');