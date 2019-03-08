-- // BBB-1234-add-status
-- Migration SQL that makes the change goes here.
ALTER TABLE applicationmanagement.application ADD COLUMN application_status VARCHAR(10) NULL DEFAULT 'TODO';
UPDATE applicationmanagement.application SET application_status = 'TODO';

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE applicationmanagement.application DROP COLUMN application_status;


