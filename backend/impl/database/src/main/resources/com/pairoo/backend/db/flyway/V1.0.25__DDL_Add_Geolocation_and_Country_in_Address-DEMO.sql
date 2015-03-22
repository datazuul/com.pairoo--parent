update ADDRESS set COUNTRY='';
alter table ADDRESS add column GEOLOCATION_ID bigint;
alter table ADDRESS add constraint FKE66327D49CE4FE3A foreign key (GEOLOCATION_ID) references GEOLOCATION;
