--
--    Copyright 2010-2016 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

-- // BBB-738-updage-swaggerhub-and-db
-- Migration SQL that makes the change goes here.
ALTER TABLE applicationmanagement.application ALTER COLUMN primary_phone_no SET NOT NULL;
ALTER TABLE applicationmanagement.application ALTER COLUMN arms_adapted_veh_desc TYPE VARCHAR(255);
ALTER TABLE applicationmanagement.application ALTER COLUMN walk_other_desc TYPE VARCHAR(255);
ALTER TABLE applicationmanagement.application ALTER COLUMN eligibility_conditions TYPE VARCHAR(10000);


-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE applicationmanagement.application ALTER COLUMN primary_phone_no DROP NOT NULL;
ALTER TABLE applicationmanagement.application ALTER COLUMN arms_adapted_veh_desc TYPE VARCHAR(100);
ALTER TABLE applicationmanagement.application ALTER COLUMN walk_other_desc TYPE VARCHAR(100);
ALTER TABLE applicationmanagement.application ALTER COLUMN eligibility_conditions TYPE VARCHAR(100);
