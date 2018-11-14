SET search_path TO applicationmanagement;

-- Use an invalid postcode and name to delete test data
DELETE FROM applicationmanagement.application WHERE contact_postcode = 'ZZ111ZZ' AND holder_name IN('TestDeleteMe','PersonDeleteMe');

DELETE FROM applicationmanagement.application WHERE id IN('0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid,'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid);
DELETE FROM applicationmanagement.app_walking_type WHERE application_id IN('0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid,'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid);
DELETE FROM applicationmanagement.app_walking_aid WHERE application_id IN('0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid,'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid);
DELETE FROM applicationmanagement.app_vehicle WHERE application_id IN('0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid,'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid);
DELETE FROM applicationmanagement.app_treatment WHERE application_id IN('0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid,'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid);
DELETE FROM applicationmanagement.app_medication WHERE application_id IN('0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid,'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid);
DELETE FROM applicationmanagement.app_healthcare_professional WHERE application_id IN('0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid,'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid);

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
 
 -- For deletion ---
 INSERT INTO applicationmanagement.application(
 id, local_authority_code, app_type_code, is_payment_taken, submission_datetime, party_code
 , contact_name, contact_building_street, contact_town_city, contact_postcode
 , holder_name, existing_badge_no, contact_line2, primary_phone_no, secondary_phone_no
 , contact_email_address, org_is_charity, org_charity_no, no_of_badges, nino
 , dob, gender_code, holder_name_at_birth, eligibility_code, eligibility_conditions
 , benefit_is_indefinite, benefit_expiry_date, walk_other_desc, walk_length_code
 , walk_speed_code, arms_driving_freq, arms_is_adapted_vehicle, arms_adapted_veh_desc
 , blind_registered_at_la_code, bulky_equipment_type_code
 , is_deleted
 ) VALUES (
 '0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'LIVER', 'NEW', true, '2011-01-01 03:00:00'::TIMESTAMP , 'PERSON'
 , 'TestDeleteMe', 'Contact Building Street', 'Contact Town City', 'ZZ111ZZ'
 , 'Holder Name', 'AAAAAA', 'Contact Line2', 'PPN', 'SPN'
 , 'Contact Email Address', true, 'Org Charity No', 1, 'Nino'
 , '1970-05-29'::DATE, 'MALE', 'Holder Name At Birth', 'DLA', 'Eligibility Conditions'
 , true, '2020-01-31'::DATE, 'Walk Other Desc', 'LESSMIN'
 , 'SLOW', 'Arms Driving Freq', true, 'Arms Adapted Veh Desc'
 , 'LIVER', 'SUCTION'
 , false
 );
INSERT INTO applicationmanagement.app_healthcare_professional(
application_id, prof_name, prof_location
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Prof Name', 'Prof Location'
);
INSERT INTO applicationmanagement.app_healthcare_professional(
application_id, prof_name, prof_location
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Prof Name2', 'Prof Location2'
);
INSERT INTO applicationmanagement.app_medication(
application_id, med_name, med_is_prescribed, med_quantity, med_frequency
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Med Name', true, 'Med Quantity', 'Med Frequency'
);
INSERT INTO applicationmanagement.app_medication(
application_id, med_name, med_is_prescribed, med_quantity, med_frequency
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Med Name2', true, 'Med Quantity2', 'Med Frequency2'
);
INSERT INTO applicationmanagement.app_treatment(
application_id, treatment_description, treatment_time
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Description', 'Time'
);
INSERT INTO applicationmanagement.app_treatment(
application_id, treatment_description, treatment_time
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Description2', 'Time2'
);
INSERT INTO applicationmanagement.app_vehicle(
application_id, registration_no, type_code, usage_frequency
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'ER1', 'CAR', 'Usage Frequency'
);
INSERT INTO applicationmanagement.app_vehicle(
application_id, registration_no, type_code, usage_frequency
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'ER2', 'CAR', 'Usage Frequency2'
);
INSERT INTO applicationmanagement.app_walking_aid(
application_id, aid_how_provided_code, aid_description, aid_usage
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'PRIVATE', 'Aid Description', 'Aid Usage'
);
INSERT INTO applicationmanagement.app_walking_aid(
application_id, aid_how_provided_code, aid_description, aid_usage
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'PRIVATE', 'Aid Description2', 'Aid Usage2'
);
INSERT INTO applicationmanagement.app_walking_type(
application_id, walking_type_code
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'PAIN'
);
INSERT INTO applicationmanagement.app_walking_type(
application_id, walking_type_code
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'BREATH'
);



 INSERT INTO applicationmanagement.application(
 id, local_authority_code, app_type_code, is_payment_taken, submission_datetime, party_code
 , contact_name, contact_building_street, contact_town_city, contact_postcode
 , holder_name, existing_badge_no, contact_line2, primary_phone_no, secondary_phone_no
 , contact_email_address, org_is_charity, org_charity_no, no_of_badges, nino
 , dob, gender_code, holder_name_at_birth, eligibility_code, eligibility_conditions
 , benefit_is_indefinite, benefit_expiry_date, walk_other_desc, walk_length_code
 , walk_speed_code, arms_driving_freq, arms_is_adapted_vehicle, arms_adapted_veh_desc
 , blind_registered_at_la_code, bulky_equipment_type_code
 , is_deleted
 ) VALUES (
 '7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'ABERD', 'NEW', true, '2011-01-01 03:00:00'::TIMESTAMP , 'PERSON'
 , 'TestDeleteMe', 'Contact Building Street', 'Contact Town City', 'ZZ111ZZ'
 , 'Holder Name', 'AAAAAA', 'Contact Line2', 'PPN', 'SPN'
 , 'Contact Email Address', true, 'Org Charity No', 1, 'Nino'
 , '1970-05-29'::DATE, 'MALE', 'Holder Name At Birth', 'DLA', 'Eligibility Conditions'
 , true, '2020-01-31'::DATE, 'Walk Other Desc', 'LESSMIN'
 , 'SLOW', 'Arms Driving Freq', true, 'Arms Adapted Veh Desc'
 , 'ABERD', 'SUCTION'
 , false
 );
INSERT INTO applicationmanagement.app_healthcare_professional(
application_id, prof_name, prof_location
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'Prof Name', 'Prof Location'
);
INSERT INTO applicationmanagement.app_healthcare_professional(
application_id, prof_name, prof_location
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'Prof Name2', 'Prof Location2'
);
INSERT INTO applicationmanagement.app_medication(
application_id, med_name, med_is_prescribed, med_quantity, med_frequency
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'Med Name', true, 'Med Quantity', 'Med Frequency'
);
INSERT INTO applicationmanagement.app_medication(
application_id, med_name, med_is_prescribed, med_quantity, med_frequency
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'Med Name2', true, 'Med Quantity2', 'Med Frequency2'
);
INSERT INTO applicationmanagement.app_treatment(
application_id, treatment_description, treatment_time
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'Description', 'Time'
);
INSERT INTO applicationmanagement.app_treatment(
application_id, treatment_description, treatment_time
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'Description2', 'Time2'
);
INSERT INTO applicationmanagement.app_vehicle(
application_id, registration_no, type_code, usage_frequency
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'ER1', 'CAR', 'Usage Frequency'
);
INSERT INTO applicationmanagement.app_vehicle(
application_id, registration_no, type_code, usage_frequency
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'ER2', 'CAR', 'Usage Frequency2'
);
INSERT INTO applicationmanagement.app_walking_aid(
application_id, aid_how_provided_code, aid_description, aid_usage
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'PRIVATE', 'Aid Description', 'Aid Usage'
);
INSERT INTO applicationmanagement.app_walking_aid(
application_id, aid_how_provided_code, aid_description, aid_usage
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'PRIVATE', 'Aid Description2', 'Aid Usage2'
);
INSERT INTO applicationmanagement.app_walking_type(
application_id, walking_type_code
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'PAIN'
);
INSERT INTO applicationmanagement.app_walking_type(
application_id, walking_type_code
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'BREATH'
);

