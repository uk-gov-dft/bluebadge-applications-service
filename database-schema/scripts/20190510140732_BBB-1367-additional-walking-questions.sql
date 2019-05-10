-- // BBB-1367-additional-walking-questions
-- Migration SQL that makes the change goes here.
ALTER TABLE applicationmanagement.application
    ADD COLUMN pain_desc VARCHAR(2000),
    ADD COLUMN balance_desc VARCHAR(2000),
    ADD COLUMN balance_health_prof_falls BOOLEAN,
    ADD COLUMN danger_desc VARCHAR(2000),
    ADD COLUMN danger_chest_lung_heart BOOLEAN
;

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE applicationmanagement.application
    DROP COLUMN pain_desc,
    DROP COLUMN balance_desc,
    DROP COLUMN balance_health_prof_falls,
    DROP COLUMN danger_desc,
    DROP COLUMN danger_chest_lung_heart
;


