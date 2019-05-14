-- // BBB-1367-additional-walking-questions
-- Migration SQL that makes the change goes here.
ALTER TABLE applicationmanagement.application
    ADD COLUMN walk_pain_desc VARCHAR(2000),
    ADD COLUMN walk_balance_desc VARCHAR(2000),
    ADD COLUMN walk_balance_health_prof_falls BOOLEAN,
    ADD COLUMN walk_danger_desc VARCHAR(2000),
    ADD COLUMN walk_danger_chest_lung_heart BOOLEAN,
    ALTER COLUMN walk_other_desc TYPE VARCHAR(2000)
;

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE applicationmanagement.application
    DROP COLUMN walk_pain_desc,
    DROP COLUMN walk_balance_desc,
    DROP COLUMN walk_balance_health_prof_falls,
    DROP COLUMN walk_danger_desc,
    DROP COLUMN walk_danger_chest_lung_heart,
    ALTER COLUMN walk_other_desc TYPE VARCHAR(255)
;


