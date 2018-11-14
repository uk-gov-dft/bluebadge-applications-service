
-- // BBB-919-artifacts
-- Migration SQL that makes the change goes here.

alter table applicationmanagement.application drop column url_proof_eligibility;
alter table applicationmanagement.application drop column url_proof_address;
alter table applicationmanagement.application drop column url_proof_identity;
alter table applicationmanagement.application drop column url_badge_photo;

create table applicationmanagement.app_artifact
(
  application_id  uuid not null
    constraint app_artifact_application_id_fk
    references application,
  artifact_type   varchar(30),
  link            varchar(256)
);

create index app_artifact_app_id_ix
  on app_artifact (application_id);

-- //@UNDO
-- SQL to undo the change goes here.

alter table applicationmanagement.application add column url_proof_eligibility varchar (256);
alter table applicationmanagement.application add column url_proof_address varchar (256);
alter table applicationmanagement.application add column url_proof_identity varchar (256);
alter table applicationmanagement.application add column url_badge_photo varchar (256);

drop index app_artifact_app_id_ix;
drop table applicationmanagement.app_artifact;
