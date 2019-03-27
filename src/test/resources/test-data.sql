
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;


DROP SCHEMA IF EXISTS applicationmanagement_unittest CASCADE;
CREATE SCHEMA applicationmanagement_unittest;
SET search_path = applicationmanagement_unittest;

CREATE TABLE applicationmanagement_unittest.app_healthcare_professional (
    application_id uuid NOT NULL,
    prof_name character varying(100),
    prof_location character varying(100)
);

CREATE TABLE applicationmanagement_unittest.app_medication (
    application_id uuid NOT NULL,
    med_name character varying(100),
    med_is_prescribed boolean,
    med_quantity character varying(100),
    med_frequency character varying(100)
);

CREATE TABLE applicationmanagement_unittest.app_treatment (
    application_id uuid NOT NULL,
    treatment_description character varying(100),
    treatment_time character varying(100)
);

CREATE TABLE applicationmanagement_unittest.app_vehicle (
    application_id uuid NOT NULL,
    registration_no character varying(7),
    type_code character varying(10),
    usage_frequency character varying(100)
);

CREATE TABLE applicationmanagement_unittest.app_walking_aid (
    application_id uuid NOT NULL,
    aid_how_provided_code character varying(10),
    aid_description character varying(100),
    aid_usage character varying(100)
);

CREATE TABLE applicationmanagement_unittest.app_walking_type (
    application_id uuid NOT NULL,
    walking_type_code character varying(10) NOT NULL
);

CREATE TABLE applicationmanagement_unittest.app_breathlessness_type (
    application_id uuid NOT NULL,
    breathlessness_type_code character varying(10) NOT NULL
);

CREATE TABLE applicationmanagement_unittest.app_bulky_equipment_type (
    application_id uuid NOT NULL,
    bulky_equipment_type_code character varying(10) NOT NULL
);

CREATE TABLE applicationmanagement_unittest.application (
    id uuid NOT NULL,
    local_authority_code character varying(10) NOT NULL,
    app_type_code character varying(10) NOT NULL,
    is_payment_taken boolean NOT NULL,
    payment_reference varchar(32),
    submission_datetime timestamp without time zone NOT NULL,
    existing_badge_no character varying(6),
    party_code character varying(10) NOT NULL,
    contact_name character varying(100),
    contact_building_street character varying(100) NOT NULL,
    contact_line2 character varying(100),
    contact_town_city character varying(100) NOT NULL,
    contact_postcode character varying(8) NOT NULL,
    primary_phone_no character varying(20),
    secondary_phone_no character varying(20),
    contact_email_address character varying(100),
    holder_name character varying(100) NOT NULL,
    org_is_charity boolean,
    org_charity_no character varying(100),
    no_of_badges integer,
    nino character varying(9),
    dob date,
    gender_code character varying(10),
    holder_name_at_birth character varying(100),
    eligibility_code character varying(10),
    eligibility_conditions character varying(100),
    benefit_is_indefinite boolean,
    benefit_expiry_date date,
    walk_other_desc character varying(100),
    breathlessness_other_desc character varying(100),
    walk_length_code character varying(10),
    walk_speed_code character varying(10),
    arms_driving_freq character varying(100),
    arms_is_adapted_vehicle boolean,
    arms_adapted_veh_desc character varying(100),
    blind_registered_at_la_code character varying(10),
    bulky_equipment_type_code character varying(10),
    bulky_equipment_other_desc character varying(100),
    is_deleted boolean DEFAULT false NOT NULL,
    deleted_timestamp date,
    application_status character varying(11) default 'TODO' NULL,
    transferred_la_from_code VARCHAR(10),
    transferred_from_la_datetime TIMESTAMP without time zone
);

ALTER TABLE ONLY applicationmanagement_unittest.app_walking_type
    ADD CONSTRAINT app_walking_type_pkey PRIMARY KEY (application_id, walking_type_code);
ALTER TABLE ONLY applicationmanagement_unittest.application
    ADD CONSTRAINT application_pkey PRIMARY KEY (id);

create table applicationmanagement_unittest.app_artifact
(
  application_id  uuid not null
    constraint app_artifact_application_id_fk
    references application,
  artifact_type   varchar(30),
  link            varchar(256)
);

CREATE INDEX app_healthcare_professional_application_id_ix ON applicationmanagement_unittest.app_healthcare_professional USING btree (application_id);
CREATE INDEX app_medication_application_id ON applicationmanagement_unittest.app_medication USING btree (application_id);
CREATE INDEX app_treatment_application_id_ix ON applicationmanagement_unittest.app_treatment USING btree (application_id);
CREATE INDEX app_vehicle_app_id_ix ON applicationmanagement_unittest.app_vehicle USING btree (application_id);
CREATE INDEX app_walking_aid_application_id_ix ON applicationmanagement_unittest.app_walking_aid USING btree (application_id);
CREATE INDEX application_authority_and_type_ix ON applicationmanagement_unittest.application USING btree (local_authority_code, app_type_code, submission_datetime DESC);
CREATE INDEX application_contact_postcode_ix ON applicationmanagement_unittest.application USING btree (contact_postcode);
CREATE INDEX application_submission_datetime_ix ON applicationmanagement_unittest.application USING btree (submission_datetime DESC, local_authority_code);

ALTER TABLE ONLY applicationmanagement_unittest.app_healthcare_professional
    ADD CONSTRAINT app_healthcare_professional_application_id_fk FOREIGN KEY (application_id) REFERENCES applicationmanagement_unittest.application(id) ON DELETE CASCADE;
ALTER TABLE ONLY applicationmanagement_unittest.app_medication
    ADD CONSTRAINT app_medication_application_id_fk FOREIGN KEY (application_id) REFERENCES applicationmanagement_unittest.application(id) ON DELETE CASCADE;
ALTER TABLE ONLY applicationmanagement_unittest.app_treatment
    ADD CONSTRAINT app_treatment_application_id_fk FOREIGN KEY (application_id) REFERENCES applicationmanagement_unittest.application(id) ON DELETE CASCADE;
ALTER TABLE ONLY applicationmanagement_unittest.app_vehicle
    ADD CONSTRAINT app_vehicle_application_id_fk FOREIGN KEY (application_id) REFERENCES applicationmanagement_unittest.application(id) ON DELETE CASCADE;
ALTER TABLE ONLY applicationmanagement_unittest.app_walking_aid
    ADD CONSTRAINT app_walking_aid_application_id_fk FOREIGN KEY (application_id) REFERENCES applicationmanagement_unittest.application(id) ON DELETE CASCADE;
ALTER TABLE ONLY applicationmanagement_unittest.app_walking_type
    ADD CONSTRAINT app_walking_type_application_id_fk FOREIGN KEY (application_id) REFERENCES applicationmanagement_unittest.application(id) ON DELETE CASCADE;


CREATE OR REPLACE FUNCTION applicationmanagement_unittest.insert_data(p_rows integer, p_la varchar) RETURNS integer
AS '
DECLARE
counter INTEGER := 0 ;
BEGIN

 LOOP
 EXIT WHEN counter = p_rows ;
 counter := counter + 1 ;

 insert into applicationmanagement_unittest.application(
 id, local_authority_code, app_type_code, is_payment_taken, submission_datetime, party_code
 , contact_name, contact_building_street, contact_town_city, contact_postcode, holder_name
 --, primary_phone_no
 ) VALUES (
 md5(random()::text || clock_timestamp()::text)::uuid, p_la, ''NEW'', true, current_timestamp, ''PERSON''
 , CONCAT(counter, ''Person''), CONCAT(counter, ''Street''), ''Atown'', CONCAT(''WV1'', counter, ''AW'')
 , CONCAT(counter, ''Holder'')
 --, ''A1234635981''
 );

 END LOOP ;

RETURN 1;
END;' LANGUAGE plpgsql;

-- Add 60 ABERD applications.
SELECT applicationmanagement_unittest.insert_data(60, 'ABERD');

-- Add 10 for XXXXX (a nonexistant la)
SELECT applicationmanagement_unittest.insert_data(10, 'XXXXXX');

-- Add some specific ones.
-- Holder 'Holder Name', LA ABERD, Type REPLACE
INSERT INTO applicationmanagement_unittest.application(
 id, local_authority_code, app_type_code, is_payment_taken, payment_reference, submission_datetime, party_code
 , contact_name, contact_building_street, contact_town_city, contact_postcode
 , holder_name, existing_badge_no, contact_line2, primary_phone_no, secondary_phone_no
 , contact_email_address, org_is_charity, org_charity_no, no_of_badges, nino
 , dob, gender_code, holder_name_at_birth, eligibility_code, eligibility_conditions
 , benefit_is_indefinite, benefit_expiry_date, walk_other_desc, breathlessness_other_desc, walk_length_code
 , walk_speed_code, arms_driving_freq, arms_is_adapted_vehicle, arms_adapted_veh_desc
 , blind_registered_at_la_code, bulky_equipment_other_desc, application_status, transferred_la_from_code
 , transferred_from_la_datetime
 ) VALUES (
 '1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'ABERD', 'REPLACE', true, 'mypayref', '2011-01-01 03:00:00'::TIMESTAMP , 'PERSON'
 , 'Contact Name', 'Contact Building Street', 'Contact Town City', 'ZZ111ZZ'
 , 'Holder Name', 'AAAAAA', 'Contact Line2', 'PPN', 'SPN'
 , 'Contact Email Address', true, 'Org Charity No', 1, 'Nino'
 , '1970-05-29'::DATE, 'MALE', 'Holder Name At Birth', 'DLA', 'Eligibility Conditions'
 , true, '2020-01-31'::DATE, 'Walk Other Desc', 'Breathlessness Other Desc', 'LESSMIN'
 , 'SLOW', 'Arms Driving Freq', true, 'Arms Adapted Veh Desc'
 , 'BIRM', 'Bulky1', 'TODO', 'ABERD'
 , '2010-12-31 03:15:00'::TIMESTAMP
 );
INSERT INTO applicationmanagement_unittest.app_healthcare_professional(
application_id, prof_name, prof_location
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'Prof Name', 'Prof Location'
);
INSERT INTO applicationmanagement_unittest.app_healthcare_professional(
application_id, prof_name, prof_location
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'Prof Name2', 'Prof Location2'
);
INSERT INTO applicationmanagement_unittest.app_medication(
application_id, med_name, med_is_prescribed, med_quantity, med_frequency
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'Med Name', true, 'Med Quantity', 'Med Frequency'
);
INSERT INTO applicationmanagement_unittest.app_medication(
application_id, med_name, med_is_prescribed, med_quantity, med_frequency
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'Med Name2', true, 'Med Quantity2', 'Med Frequency2'
);
INSERT INTO applicationmanagement_unittest.app_treatment(
application_id, treatment_description, treatment_time
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'Description', 'Time'
);
INSERT INTO applicationmanagement_unittest.app_treatment(
application_id, treatment_description, treatment_time
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'Description2', 'Time2'
);
INSERT INTO applicationmanagement_unittest.app_vehicle(
application_id, registration_no, type_code, usage_frequency
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'ER1', 'CAR', 'Usage Frequency'
);
INSERT INTO applicationmanagement_unittest.app_vehicle(
application_id, registration_no, type_code, usage_frequency
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'ER2', 'CAR', 'Usage Frequency2'
);
INSERT INTO applicationmanagement_unittest.app_walking_aid(
application_id, aid_how_provided_code, aid_description, aid_usage
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'PRIVATE', 'Aid Description', 'Aid Usage'
);
INSERT INTO applicationmanagement_unittest.app_walking_aid(
application_id, aid_how_provided_code, aid_description, aid_usage
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'PRIVATE', 'Aid Description2', 'Aid Usage2'
);
INSERT INTO applicationmanagement_unittest.app_walking_type(
application_id, walking_type_code
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'PAIN'
);
INSERT INTO applicationmanagement_unittest.app_walking_type(
application_id, walking_type_code
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'BREATH'
);
INSERT INTO applicationmanagement_unittest.app_breathlessness_type(
application_id, breathlessness_type_code
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'UPHILL'
);
INSERT INTO applicationmanagement_unittest.app_breathlessness_type(
application_id, breathlessness_type_code
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'OTHER'
);
INSERT INTO applicationmanagement_unittest.app_bulky_equipment_type(
application_id, bulky_equipment_type_code
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'SUCTION'
);
INSERT INTO applicationmanagement_unittest.app_bulky_equipment_type(
application_id, bulky_equipment_type_code
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'OTHER'
);
INSERT INTO applicationmanagement_unittest.app_artifact(
application_id, artifact_type, link
) VALUES (
'1087ac26-491a-46f0-9006-36187dc40764'::uuid, 'PROOF_ID', 'link/to/artifact1'
);

-- Submitted 10 years ago - a cancel app.
INSERT INTO applicationmanagement_unittest.application(
 id, local_authority_code, app_type_code, is_payment_taken, submission_datetime, party_code
 , contact_name, contact_building_street, contact_town_city, contact_postcode
 , holder_name
 ) VALUES (
 md5(random()::text || clock_timestamp()::text)::uuid, 'ABERD', 'CANCEL', true, current_timestamp - interval '10 years', 'PERSON'
 , null, 'Street', 'Atown', 'XY111ZZ'
 , '10years back'
 );

 -- application to delete
INSERT INTO applicationmanagement_unittest.application(
 id, local_authority_code, app_type_code, is_payment_taken, payment_reference, submission_datetime, party_code
 , contact_name, contact_building_street, contact_town_city, contact_postcode
 , holder_name, existing_badge_no, contact_line2, primary_phone_no, secondary_phone_no
 , contact_email_address, org_is_charity, org_charity_no, no_of_badges, nino
 , dob, gender_code, holder_name_at_birth, eligibility_code, eligibility_conditions
 , benefit_is_indefinite, benefit_expiry_date, walk_other_desc, breathlessness_other_desc, walk_length_code
 , walk_speed_code, arms_driving_freq, arms_is_adapted_vehicle, arms_adapted_veh_desc
 , blind_registered_at_la_code
 , is_deleted
 ) VALUES (
 '0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'LIVER', 'NEW', true, 'mypayref', '2011-01-01 03:00:00'::TIMESTAMP , 'PERSON'
 , 'Contact Name', 'Contact Building Street', 'Contact Town City', 'ZZ111ZZ'
 , 'Holder Name', 'AAAAAA', 'Contact Line2', 'PPN', 'SPN'
 , 'Contact Email Address', true, 'Org Charity No', 1, 'Nino'
 , '1970-05-29'::DATE, 'MALE', 'Holder Name At Birth', 'DLA', 'Eligibility Conditions'
 , true, '2020-01-31'::DATE, 'Walk Other Desc', 'Breathlessness Other Desc', 'LESSMIN'
 , 'SLOW', 'Arms Driving Freq', true, 'Arms Adapted Veh Desc'
 , 'LIVER'
 , false
 );
INSERT INTO applicationmanagement_unittest.app_healthcare_professional(
application_id, prof_name, prof_location
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Prof Name', 'Prof Location'
);
INSERT INTO applicationmanagement_unittest.app_healthcare_professional(
application_id, prof_name, prof_location
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Prof Name2', 'Prof Location2'
);
INSERT INTO applicationmanagement_unittest.app_medication(
application_id, med_name, med_is_prescribed, med_quantity, med_frequency
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Med Name', true, 'Med Quantity', 'Med Frequency'
);
INSERT INTO applicationmanagement_unittest.app_medication(
application_id, med_name, med_is_prescribed, med_quantity, med_frequency
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Med Name2', true, 'Med Quantity2', 'Med Frequency2'
);
INSERT INTO applicationmanagement_unittest.app_treatment(
application_id, treatment_description, treatment_time
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Description', 'Time'
);
INSERT INTO applicationmanagement_unittest.app_treatment(
application_id, treatment_description, treatment_time
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'Description2', 'Time2'
);
INSERT INTO applicationmanagement_unittest.app_vehicle(
application_id, registration_no, type_code, usage_frequency
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'ER1', 'CAR', 'Usage Frequency'
);
INSERT INTO applicationmanagement_unittest.app_vehicle(
application_id, registration_no, type_code, usage_frequency
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'ER2', 'CAR', 'Usage Frequency2'
);
INSERT INTO applicationmanagement_unittest.app_walking_aid(
application_id, aid_how_provided_code, aid_description, aid_usage
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'PRIVATE', 'Aid Description', 'Aid Usage'
);
INSERT INTO applicationmanagement_unittest.app_walking_aid(
application_id, aid_how_provided_code, aid_description, aid_usage
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'PRIVATE', 'Aid Description2', 'Aid Usage2'
);
INSERT INTO applicationmanagement_unittest.app_walking_type(
application_id, walking_type_code
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'PAIN'
);
INSERT INTO applicationmanagement_unittest.app_walking_type(
application_id, walking_type_code
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'BREATH'
);
INSERT INTO applicationmanagement_unittest.app_breathlessness_type(
application_id, breathlessness_type_code
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'UPHILL'
);
INSERT INTO applicationmanagement_unittest.app_breathlessness_type(
application_id, breathlessness_type_code
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'OTHER'
);
INSERT INTO applicationmanagement_unittest.app_bulky_equipment_type(
application_id, bulky_equipment_type_code
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'SUCTION'
);
INSERT INTO applicationmanagement_unittest.app_bulky_equipment_type(
application_id, bulky_equipment_type_code
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'VENT'
);
INSERT INTO applicationmanagement_unittest.app_artifact(
application_id, artifact_type, link
) VALUES (
'0bd06c01-a193-4255-be0b-0fbee253ee5e'::uuid, 'PROOF_ID', 'link/to/artifact'
);

