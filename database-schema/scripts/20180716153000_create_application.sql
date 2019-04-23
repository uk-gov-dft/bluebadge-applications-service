
CREATE TABLE applicationmanagement.application (
-- Application fields
  id                    UUID            NOT NULL
 ,local_authority_code  VARCHAR(10)     NOT NULL
 ,app_type_code         VARCHAR(10)     NOT NULL
 ,is_payment_taken      BOOLEAN         NOT NULL
 ,submission_datetime   TIMESTAMP       NOT NULL
 ,existing_badge_no     VARCHAR(6)
 ,party_code            VARCHAR(10)     NOT NULL
-- Contact fields
 ,contact_name          VARCHAR(100)
 ,contact_building_street VARCHAR(100)  NOT NULL
 ,contact_line2         VARCHAR(100)
 ,contact_town_city     VARCHAR(100)    NOT NULL
 ,contact_postcode      VARCHAR(8)      NOT NULL
 ,primary_phone_no      VARCHAR(20)
 ,secondary_phone_no    VARCHAR(20)
 ,contact_email_address VARCHAR(100)
-- Person or Org fields
 ,holder_name           VARCHAR(100)    NOT NULL
 ,org_is_charity        BOOLEAN
 ,org_charity_no        VARCHAR(100)
 ,no_of_badges          INTEGER
 ,nino                  VARCHAR(9)
 ,dob                   DATE
 ,gender_code           VARCHAR(10)
 ,holder_name_at_birth  VARCHAR(100)
-- Eligibility
 ,eligibility_code      VARCHAR(10)
 ,eligibility_conditions VARCHAR(100)
 ,benefit_is_indefinite BOOLEAN
 ,benefit_expiry_date   DATE
 ,walk_other_desc       VARCHAR(100)
 ,walk_length_code      VARCHAR(10)
 ,walk_speed_code       VARCHAR(10)
 ,arms_driving_freq     VARCHAR(100)
 ,arms_is_adapted_vehicle BOOLEAN
 ,arms_adapted_veh_desc VARCHAR(100)
 ,blind_registered_at_la_code VARCHAR(10)
 ,bulky_equipment_type_code VARCHAR(10)
 ,url_proof_eligibility VARCHAR(255)
 ,url_proof_address     VARCHAR(255)
 ,url_proof_identity    VARCHAR(255)
 ,url_badge_photo       VARCHAR(255)
 ,PRIMARY KEY(id)
);

COMMENT ON TABLE application IS 'Holds blue badge application record.';
COMMENT ON COLUMN application.id IS 'PK, a UUID.';
COMMENT ON COLUMN application.local_authority_code IS 'Local authority.';
COMMENT ON COLUMN application.app_type_code IS 'NEW, CANCEL, RENEW, REPLACE';
COMMENT ON COLUMN application.is_payment_taken IS 'true if payment received as part of online application.';
COMMENT ON COLUMN application.submission_datetime IS 'Date and time application created.';
COMMENT ON COLUMN application.existing_badge_no IS 'For a replacement, cancellation or renewal application.';
COMMENT ON COLUMN application.party_code IS 'Person or organisation ref code.';
COMMENT ON COLUMN application.contact_name IS 'Application contact name for organisation.';
COMMENT ON COLUMN application.contact_building_street IS 'Applicant address line 1.';
COMMENT ON COLUMN application.contact_line2 IS 'Applicant address line 2.';
COMMENT ON COLUMN application.contact_town_city IS 'Applicant town or city (or hamlet etc.).';
COMMENT ON COLUMN application.contact_postcode IS 'Applicant postcode.';
COMMENT ON COLUMN application.primary_phone_no IS 'Applicant main contact number.';
COMMENT ON COLUMN application.secondary_phone_no IS 'Applicant secondary contact number.';
COMMENT ON COLUMN application.contact_email_address IS 'Applicant email address.';
COMMENT ON COLUMN application.holder_name IS 'Badge holder name, for person or organisation.';
COMMENT ON COLUMN application.org_is_charity IS 'true if party is an organisation and a charity.';
COMMENT ON COLUMN application.org_charity_no IS 'Charity number if party is an organisation and a charity.';
COMMENT ON COLUMN application.no_of_badges IS 'Number of badges being applied for. Can only be 1 if a person.';
COMMENT ON COLUMN application.nino IS 'If person, national insurance number.';
COMMENT ON COLUMN application.dob IS 'If person, date of birth.';
COMMENT ON COLUMN application.gender_code IS 'MALE, FEMALE.';
COMMENT ON COLUMN application.holder_name_at_birth IS 'If applicant a person, then name at birth if changed.';
COMMENT ON COLUMN application.eligibility_code IS 'Ref data code of eligibility reason.';
COMMENT ON COLUMN application.eligibility_conditions IS 'Description of eligibility conditions';
COMMENT ON COLUMN application.benefit_is_indefinite IS 'true if applicant is receiving a benefit indefinitely.';
COMMENT ON COLUMN application.benefit_expiry_date IS 'Expiry date of benefit if not indefinite.';
COMMENT ON COLUMN application.walk_other_desc IS 'Description of walking difficulty not covered by walking type codes.';
COMMENT ON COLUMN application.walk_length_code IS 'From ref data group WALKLEN, e.g. LESSMIN, FIVETEN.';
COMMENT ON COLUMN application.walk_speed_code IS 'From ref data group WALKSPEED, e.g. FAST, SAME.';
COMMENT ON COLUMN application.arms_driving_freq IS 'Freetext description of driving frequency.';
COMMENT ON COLUMN application.arms_is_adapted_vehicle IS 'true if applicants vehicle is adapted for disibility of arms.';
COMMENT ON COLUMN application.arms_adapted_veh_desc IS 'Freetext description of vehicle adaptions.';
COMMENT ON COLUMN application.blind_registered_at_la_code IS 'If registered blind, then the local authority holding the registration.';
COMMENT ON COLUMN application.bulky_equipment_type_code IS 'From ref data group BULKEQUIP.';
COMMENT ON COLUMN application.url_proof_eligibility IS 'Location of uploaded eligibility proof documentation.';
COMMENT ON COLUMN application.url_proof_address IS 'Location of uploaded proof of address documentation.';
COMMENT ON COLUMN application.url_proof_identity IS 'Location of uploaded proof of address documentation.';
COMMENT ON COLUMN application.url_badge_photo IS 'Location of uploaded badge photograph.';

CREATE TABLE applicationmanagement.app_vehicle(
  application_id      UUID         NOT NULL
 ,registration_no     VARCHAR(7)   NOT NULL
 ,type_code           VARCHAR(10)
 ,usage_frequency     VARCHAR(100)
 ,PRIMARY KEY(application_id, registration_no)
);

ALTER TABLE applicationmanagement.app_vehicle
  ADD CONSTRAINT app_vehicle_application_id_fk
  FOREIGN KEY (application_id)
  REFERENCES application(id)
  ON DELETE CASCADE;

COMMENT ON TABLE app_vehicle IS 'Holds details of an organisations vehicles.';
COMMENT ON COLUMN app_vehicle.application_id IS 'FK to application.';
COMMENT ON COLUMN app_vehicle.registration_no IS 'Vehicle registration number.';
COMMENT ON COLUMN app_vehicle.type_code IS 'From reference data group VEHICLETYP e.g. CAR.';

CREATE TABLE applicationmanagement.app_walking_type(
  application_id      UUID         NOT NULL
 ,walking_type_code   VARCHAR(10)  NOT NULL
 ,PRIMARY KEY (application_id, walking_type_code)
);

ALTER TABLE applicationmanagement.app_walking_type
  ADD CONSTRAINT app_walking_type_application_id_fk
  FOREIGN KEY (application_id)
  REFERENCES application(id)
  ON DELETE CASCADE;

COMMENT ON TABLE app_walking_type IS 'Holds short codes from the WALKDIFF group of data for an application.';
COMMENT ON COLUMN app_walking_type.application_id IS 'FK to application.';
COMMENT ON COLUMN app_walking_type.walking_type_code IS 'The key, e.g. BALANCE or PAIN.';

CREATE TABLE applicationmanagement.app_walking_aid(
  application_id           UUID            NOT NULL
 ,aid_how_provided_code    VARCHAR(10)
 ,aid_description          VARCHAR(100)
 ,aid_usage                VARCHAR(100)
);

CREATE INDEX app_walking_aid_application_id_ix
  ON applicationmanagement.app_walking_aid(application_id);

ALTER TABLE applicationmanagement.app_walking_aid
  ADD CONSTRAINT app_walking_aid_application_id_fk
  FOREIGN KEY (application_id)
  REFERENCES application(id)
  ON DELETE CASCADE;

COMMENT ON TABLE app_walking_aid IS 'Holds short codes from the WALKMOB group of data for an application.';
COMMENT ON COLUMN app_walking_aid.application_id IS 'FK to application.';
COMMENT ON COLUMN app_walking_aid.aid_how_provided_code IS 'The key, e.g. PRIVATE, SOCIAL.';
COMMENT ON COLUMN app_walking_aid.aid_description IS 'Freetext description of the walking aid.';
COMMENT ON COLUMN app_walking_aid.aid_usage IS 'Freetext description of the usage of the walking aid.';;

CREATE TABLE applicationmanagement.app_treatment(
  application_id         UUID           NOT NULL
 ,treatment_description  VARCHAR(100)
 ,treatment_time         VARCHAR(100)
);

CREATE INDEX app_treatment_application_id_ix
  ON applicationmanagement.app_treatment(application_id);

ALTER TABLE applicationmanagement.app_treatment
  ADD CONSTRAINT app_treatment_application_id_fk
  FOREIGN KEY (application_id)
  REFERENCES application(id)
  ON DELETE CASCADE;

COMMENT ON TABLE app_treatment IS 'Walking difficulty treatments for an application.';
COMMENT ON COLUMN app_treatment.application_id IS 'FK to application.';
COMMENT ON COLUMN app_treatment.treatment_description IS 'Freetext description of treatment.';
COMMENT ON COLUMN app_treatment.treatment_time IS 'Freetext description of treatment duration.';

CREATE TABLE applicationmanagement.app_medication(
  application_id        UUID          NOT NULL
 ,med_name              VARCHAR(100)
 ,med_is_prescribed     BOOLEAN
 ,med_quantity          VARCHAR(100)
 ,med_frequency         VARCHAR(100)
);

CREATE INDEX app_medication_application_id
  ON applicationmanagement.app_medication(application_id);

ALTER TABLE applicationmanagement.app_medication
  ADD CONSTRAINT app_medication_application_id_fk
  FOREIGN KEY (application_id)
  REFERENCES application(id)
  ON DELETE CASCADE;

COMMENT ON TABLE app_medication IS 'Walking difficulty medications being taken.';
COMMENT ON COLUMN app_medication.application_id IS 'FK to application.';
COMMENT ON COLUMN app_medication.med_name IS 'Freetext name of medication.';
COMMENT ON COLUMN app_medication.med_is_prescribed IS 'true if a prescribed medication.';
COMMENT ON COLUMN app_medication.med_quantity IS 'Freetext description of medication quantity.';
COMMENT ON COLUMN app_medication.med_quantity IS 'Freetext description of medication frequency.';

CREATE TABLE applicationmanagement.app_healthcare_professional(
  application_id             UUID            NOT NULL
 ,prof_name                  VARCHAR(100)
 ,prof_location              VARCHAR(100)
);

CREATE INDEX app_healthcare_professional_application_id_ix
  ON applicationmanagement.app_healthcare_professional(application_id);

ALTER TABLE applicationmanagement.app_healthcare_professional
  ADD CONSTRAINT app_healthcare_professional_application_id_fk
  FOREIGN KEY (application_id)
  REFERENCES application(id)
  ON DELETE CASCADE;

COMMENT ON TABLE app_healthcare_professional IS 'Healthcare professionals for an application.';
COMMENT ON COLUMN app_healthcare_professional.prof_name IS 'Name of the healthcare professional.';
COMMENT ON COLUMN app_healthcare_professional.prof_location IS 'Location of the healthcare professional.';

--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS applicationmanagement.app_walking_type;
DROP TABLE IF EXISTS applicationmanagement.app_vehicle;
DROP TABLE IF EXISTS applicationmanagement.app_walking_aid;
DROP TABLE IF EXISTS applicationmanagement.app_treatment;
DROP TABLE IF EXISTS applicationmanagement.app_medication;
DROP TABLE IF EXISTS applicationmanagement.app_healthcare_professional;
DROP TABLE IF EXISTS applicationmanagement.application;
