SET search_path TO applicationmanagement;

-- Use an invalid postcode and name to delete test data
DELETE FROM applicationmanagement.application WHERE contact_postcode = 'ZZ111ZZ' AND holder_name IN('TestDeleteMe','PersonDeleteMe');

-- Different local authority
INSERT INTO applicationmanagement.application(
 id, local_authority_code, app_type_code, is_payment_taken, submission_datetime, party_code
 , contact_name, contact_building_street, contact_town_city, contact_postcode
 , holder_name, primary_phone_no
 ) VALUES (
 '4cf7be77-cfe7-4c9f-a229-ea61e903fb3a'::uuid, 'BIRM', 'CANCEL', true, current_timestamp - interval '10 years', 'PERSON'
 , null, 'Street', 'Atown', 'ZZ111ZZ'
 , 'TestDeleteMe', '01234567890'
 );