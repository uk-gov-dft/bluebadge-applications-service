ALTER TABLE applicationmanagement.app_vehicle DROP CONSTRAINT app_vehicle_pkey;
CREATE INDEX app_vehicle_app_id_ix ON applicationmanagement.app_vehicle(application_id);
ALTER TABLE applicationmanagement.app_vehicle ALTER COLUMN registration_no DROP NOT NULL;

--//@UNDO
-- SQL to undo the change goes here.
-- Cant undo safely as can have nulls.