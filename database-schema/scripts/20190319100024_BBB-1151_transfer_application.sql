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

-- // BBB-1151-transfer-application
ALTER TABLE application ADD COLUMN
  transfer_la_from_code VARCHAR(10) DEFAULT null;
ALTER TABLE application ADD COLUMN
  transfer_la_datetime TIMESTAMP DEFAULT null;

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE application DROP COLUMN transfer_la_from_code;
ALTER TABLE application DROP COLUMN transfer_la_datetime;


