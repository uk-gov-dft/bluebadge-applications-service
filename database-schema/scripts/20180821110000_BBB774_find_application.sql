
-- A bit of a guess at indexing that will work for find application.  holder_name is a like
-- search, so could be problematic.
CREATE INDEX application_submission_datetime_ix ON applicationmanagement.application(submission_datetime DESC, local_authority_code);
CREATE INDEX application_authority_and_type_ix ON applicationmanagement.application(local_authority_code, app_type_code, submission_datetime DESC);
CREATE INDEX application_contact_postcode_ix ON applicationmanagement.application(contact_postcode);

--//@UNDO
-- SQL to undo the change goes here.
-- Cant undo safely as can have nulls.
DROP INDEX IF EXISTS applicationmanagement.application_submission_datetime_ix;
DROP INDEX IF EXISTS applicationmanagement.application_authority_and_type_ix;
DROP INDEX IF EXISTS applicationmanagement.application_contact_postcode_ix;
