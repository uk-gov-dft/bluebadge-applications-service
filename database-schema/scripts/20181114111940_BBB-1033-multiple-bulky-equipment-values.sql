-- // BBB-1033-multiple-bulky-equipment-values
-- Migration SQL that makes the change goes here.

CREATE TABLE applicationmanagement.app_bulky_equipment_type(
  application_id UUID NOT NULL
  , bulky_equipment_type_code VARCHAR(10) NOT NULL
  , PRIMARY KEY (application_id, bulky_equipment_type_code)
);

ALTER TABLE applicationmanagement.app_bulky_equipment_type
  ADD CONSTRAINT app_bulky_equipment_type_application_id_fk
  FOREIGN KEY (application_id) REFERENCES application(id) ON DELETE CASCADE;

COMMENT ON TABLE applicationmanagement.app_bulky_equipment_type IS 'Bulky medical equipment type codes.';
COMMENT ON COLUMN applicationmanagement.app_bulky_equipment_type.application_id IS 'FK to application.';
COMMENT ON COLUMN applicationmanagement.app_bulky_equipment_type.application_id IS 'From ref data group BULKEQUIP.';

INSERT INTO applicationmanagement.app_bulky_equipment_type
  SELECT id, bulky_equipment_type_code
  FROM applicationmanagement.application
  WHERE bulky_equipment_type_code IS NOT NULL;

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE applicationmanagement.application
WITH equipment AS
  (SELECT application_id, MAX(bulky_equipment_type_code) bulky_equipment_type_code
  FROM applicationmanagement.app_bulky_equipment_type)
UPDATE applicationmanagement.application a SET a.bulky_equipment_type_code = equipment.bulky_equipment_type_code
WHERE a.id = equipment.application_id;

DROP TABLE applicationmanagement.app_bulky_equipment_type;
