DELETE FROM users WHERE email_address = 'createuservalid@dft.gov.uk';
DELETE FROM users WHERE id < 0;
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-1, 'Sampath', 'abc@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-2, 'Sampath', 'def@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-3, 'nobody', 'abcnobody@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-4, 'update test', 'updateme@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-5, 'delete test', 'deleteme@dft.gov.uk', 2, 2);

INSERT INTO usermanagement.users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid, is_active, login_fail_count) 
VALUES (-100, 'Bruce Wayne', 'um_abc@dft.gov.uk', 'ABERD', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '3bfe600b-4425-40cd-ad81-d75bbe16ee13'::UUID, TRUE, 0 );
