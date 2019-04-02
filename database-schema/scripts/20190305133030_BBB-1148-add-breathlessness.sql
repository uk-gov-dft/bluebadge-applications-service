-- // BBB-1148-add-breathlessness
-- Migration SQL that makes the change goes here.

ALTER TABLE applicationmanagement.application ADD COLUMN breathlessness_other_desc VARCHAR(256);

CREATE TABLE applicationmanagement.app_breathlessness_type(
  application_id      UUID         NOT NULL
 ,breathlessness_type_code   VARCHAR(10)  NOT NULL
 ,PRIMARY KEY (application_id, breathlessness_type_code)
);

ALTER TABLE applicationmanagement.app_breathlessness_type
  ADD CONSTRAINT app_breathlessness_type_application_id_fk
  FOREIGN KEY (application_id)
  REFERENCES application(id)
  ON DELETE CASCADE;

COMMENT ON TABLE app_breathlessness_type IS 'Holds short codes from the BREATHLESSNESS group of data for an application.';
COMMENT ON COLUMN app_breathlessness_type.application_id IS 'FK to application.';
COMMENT ON COLUMN app_breathlessness_type.breathlessness_type_code IS 'The key, e.g. UPHILL or OWNPACE.';


-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE IF EXISTS applicationmanagement.app_breathlessness_type;
ALTER TABLE applicationmanagement.application DROP COLUMN breathlessness_other_desc;
