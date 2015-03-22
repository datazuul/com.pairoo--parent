create table GEOAREA (ID bigint not null, CONTINENT varchar(255), COUNTRY varchar(255), GEOLOCATION_ID bigint, MAXDISTANCE varchar(255), SUBDIVISION_ID bigint, ZIPCODE_START varchar(255), primary key (ID));

alter table CITY drop constraint FK1F916BBA379CBA; /* SUBDIVISION */
alter table GEOLOCATION drop constraint FK844CE1E66657EBBA; /* CITY_ID */
alter table GEOLOCATION drop constraint FK844CE1E6BA379CBA; /* SUBDIVISION_ID */
alter table SEARCHPROFILE drop constraint FK928C0EE16E1B8FE9; /* GEOLOCATION */
alter table USERPROFILE drop constraint FKF8AB675E6E1B8FE9; /* GEOLOCATION */

alter table GEOLOCATION add column NAME varchar(255);
alter table GEOLOCATION add column LATITUDE double;
alter table GEOLOCATION add column LONGITUDE double;
alter table GEOLOCATION add column POPULATION integer;
alter table GEOLOCATION add column SUBDIVISION_BIGGEST_ID bigint;
alter table GEOLOCATION add column SUBDIVISION_SMALLEST_ID bigint;

alter table LIFESTYLE add column FITNESSTYPE varchar(255);

alter table SEARCHPROFILE add column GEOAREA_ID bigint;

alter table SUBDIVISION add column COUNTRY varchar(255);
alter table SUBDIVISION add column PARENT_ID bigint;

alter table USERPROFILE add column GEOLOCATION_ID bigint;

alter table USERS alter column EMAIL varchar(256);

alter table GEOAREA add constraint FK2610DB3E2FFE2E4C foreign key (GEOLOCATION_ID) references GEOLOCATION;
alter table GEOAREA add constraint FK2610DB3EE4AA8BFA foreign key (SUBDIVISION_ID) references SUBDIVISION;
alter table GEOLOCATION add constraint FK844CE1E635324E7A foreign key (SUBDIVISION_BIGGEST_ID) references SUBDIVISION;
alter table GEOLOCATION add constraint FK844CE1E69BD0DD3E foreign key (SUBDIVISION_SMALLEST_ID) references SUBDIVISION;
alter table SEARCHPROFILE add constraint FK928C0EE195B950FC foreign key (GEOAREA_ID) references GEOAREA;
alter table SUBDIVISION add constraint FKE09042CD4918E5B7 foreign key (PARENT_ID) references SUBDIVISION;
alter table SUBDIVISION add unique (CODE, NAME, COUNTRY);
alter table USERPROFILE add constraint FKF8AB675E2FFE2E4C foreign key (GEOLOCATION_ID) references GEOLOCATION;
