-- Set up an LA Editor for use with the update application
set search_path=usermanagement;
DELETE FROM usermanagement.users WHERE id = -22;
INSERT INTO usermanagement.users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid, is_active, login_fail_count) VALUES
(-22, 'Dick Grayson', 'editor@dft.gov.uk', 'ABERD', 3, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '1dd704ed-4538-45e4-af10-e00fab8e27f1'::UUID, TRUE, 0 );


SET search_path TO applicationmanagement;

-- Fks are all cascade on delete.
DELETE FROM applicationmanagement.application WHERE contact_name LIKE 'TestDeleteMe%' OR contact_name LIKE 'PersonDeleteMe%'
OR holder_name LIKE 'TestDeleteMe%' OR holder_name LIKE 'PersonDeleteMe%';

--Thedeleted one has personal info removed so delete by id.
DELETE FROM applicationmanagement.application WHERE id = '7d93fdb5-56bf-41b3-8af0-147696711410';

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
 , blind_registered_at_la_code
 , is_deleted
 , application_status
 ) VALUES (
 '0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'LIVER', 'NEW', true, '2011-01-01 03:00:00'::TIMESTAMP , 'PERSON'
 , 'TestDeleteMe', 'Contact Building Street', 'Contact Town City', 'ZZ111ZZ'
 , 'Holder Name', 'AAAAAA', 'Contact Line2', 'PPN', 'SPN'
 , 'Contact Email Address', true, 'Org Charity No', 1, 'Nino'
 , '1970-05-29'::DATE, 'MALE', 'Holder Name At Birth', 'DLA', 'Eligibility Conditions'
 , true, '2020-01-31'::DATE, 'Walk Other Desc', 'LESSMIN'
 , 'SLOW', 'Arms Driving Freq', true, 'Arms Adapted Veh Desc'
 , 'LIVER'
 , false
 , 'IN_PROGRESS'
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
INSERT INTO applicationmanagement.app_bulky_equipment_type(
application_id, bulky_equipment_type_code
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'SUCTION'
);



 INSERT INTO applicationmanagement.application(
 id, local_authority_code, app_type_code, is_payment_taken, submission_datetime, party_code
 , contact_name, contact_building_street, contact_town_city, contact_postcode
 , holder_name, existing_badge_no, contact_line2, primary_phone_no, secondary_phone_no
 , contact_email_address, org_is_charity, org_charity_no, no_of_badges, nino
 , dob, gender_code, holder_name_at_birth, eligibility_code, eligibility_conditions
 , benefit_is_indefinite, benefit_expiry_date, walk_other_desc, walk_length_code
 , walk_speed_code, arms_driving_freq, arms_is_adapted_vehicle, arms_adapted_veh_desc
 , blind_registered_at_la_code
 , is_deleted
 , application_status
 ) VALUES (
 '7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'ABERD', 'NEW', true, '2011-01-01 03:00:00'::TIMESTAMP , 'PERSON'
 , 'TestDeleteMe', 'Contact Building Street', 'Contact Town City', 'ZZ111ZZ'
 , 'Holder Name', 'AAAAAA', 'Contact Line2', 'PPN', 'SPN'
 , 'Contact Email Address', true, 'Org Charity No', 1, 'Nino'
 , '1970-05-29'::DATE, 'MALE', 'Holder Name At Birth', 'DLA', 'Eligibility Conditions'
 , true, '2020-01-31'::DATE, 'Walk Other Desc', 'LESSMIN'
 , 'SLOW', 'Arms Driving Freq', true, 'Arms Adapted Veh Desc'
 , 'ABERD'
 , false
 , 'IN_PROGRESS'
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
INSERT INTO applicationmanagement.app_bulky_equipment_type(
application_id, bulky_equipment_type_code
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'SUCTION'
);
INSERT INTO applicationmanagement.app_artifact(
application_id, artifact_type, link
) VALUES (
'7d93fdb5-56bf-41b3-8af0-147696711410'::uuid, 'PHOTO', 'appArtifactsTestFile.png'
);

-- For retrieve Child Bulk
INSERT INTO applicationmanagement.application(
 id, local_authority_code, app_type_code, is_payment_taken, submission_datetime, party_code
 , contact_name, contact_building_street, contact_town_city, contact_postcode
 , holder_name, existing_badge_no, contact_line2, primary_phone_no, secondary_phone_no
 , contact_email_address, org_is_charity, org_charity_no, no_of_badges, nino
 , dob, gender_code, holder_name_at_birth, eligibility_code, eligibility_conditions
 , benefit_is_indefinite, benefit_expiry_date, walk_other_desc, walk_length_code
 , walk_speed_code, arms_driving_freq, arms_is_adapted_vehicle, arms_adapted_veh_desc
 , blind_registered_at_la_code
 , is_deleted
 , application_status
 ) VALUES (
 '89ca4c39-02d5-4197-b032-1d9ce22c24b5'::uuid, 'ABERD', 'NEW', true, current_timestamp, 'PERSON'
 , 'TestDeleteMe', 'Contact Building Street', 'Contact Town City', 'ZZ111ZZ'
 , 'Holder Name', 'AAAAAA', 'Contact Line2', 'PPN', 'SPN'
 , 'Contact Email Address', true, 'Org Charity No', 1, 'Nino'
 , '1970-05-29'::DATE, 'MALE', 'Holder Name At Birth', 'CHILDBULK', 'Eligibility Conditions'
 , true, '2020-01-31'::DATE, null, 'LESSMIN'
 , 'SLOW', 'Arms Driving Freq', true, 'Arms Adapted Veh Desc'
 , 'LIVER'
 , false
 , 'IN_PROGRESS'
 );

 INSERT INTO applicationmanagement.app_bulky_equipment_type(
 application_id, bulky_equipment_type_code
 ) VALUES (
 '89ca4c39-02d5-4197-b032-1d9ce22c24b5'::uuid, 'SUCTION'
 );

