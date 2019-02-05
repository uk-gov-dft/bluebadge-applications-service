-- // BBB-1161-view-payments-status
-- Migration SQL that makes the change goes here.

ALTER TABLE applicationmanagement.application ADD COLUMN
  payment_reference VARCHAR(12);

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE applicationmanagement.application DROP COLUMN payment_reference;
