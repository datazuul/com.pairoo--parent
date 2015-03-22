create table GEOAREA (ID int8 not null, CONTINENT varchar(255), COUNTRY varchar(255), GEOLOCATION_ID int8, MAXDISTANCE varchar(255), SUBDIVISION_ID int8, ZIPCODE_START varchar(255), primary key (ID));

alter table CITY drop constraint FK1F916BBA379CBA; /* SUBDIVISION */
alter table GEOLOCATION drop constraint FK844CE1E66657EBBA; /* CITY_ID */
alter table GEOLOCATION drop constraint FK844CE1E6BA379CBA; /* SUBDIVISION_ID */
alter table SEARCHPROFILE drop constraint FK928C0EE16E1B8FE9; /* GEOLOCATION */
alter table USERPROFILE drop constraint FKF8AB675E6E1B8FE9; /* GEOLOCATION */

alter table GEOLOCATION add column NAME varchar(255);
alter table GEOLOCATION add column LATITUDE float8;
alter table GEOLOCATION add column LONGITUDE float8;
alter table GEOLOCATION add column POPULATION int4;
alter table GEOLOCATION add column SUBDIVISION_BIGGEST_ID int8;
alter table GEOLOCATION add column SUBDIVISION_SMALLEST_ID int8;

alter table LIFESTYLE add column FITNESSTYPE varchar(255);

alter table SEARCHPROFILE add column GEOAREA_ID int8;

alter table SUBDIVISION add column COUNTRY varchar(255);
alter table SUBDIVISION add column PARENT_ID int8;

alter table USERPROFILE add column GEOLOCATION_ID int8;

alter table USERS alter column EMAIL type varchar(256);

alter table GEOAREA add constraint FK2610DB3E9CE4FE3A foreign key (GEOLOCATION_ID) references GEOLOCATION;
alter table GEOAREA add constraint FK2610DB3EBA379CBA foreign key (SUBDIVISION_ID) references SUBDIVISION;
alter table GEOLOCATION add constraint FK844CE1E620BACA3A foreign key (SUBDIVISION_BIGGEST_ID) references SUBDIVISION;
alter table GEOLOCATION add constraint FK844CE1E6E4294F6 foreign key (SUBDIVISION_SMALLEST_ID) references SUBDIVISION;
alter table SEARCHPROFILE add constraint FK928C0EE141247F3A foreign key (GEOAREA_ID) references GEOAREA;
alter table SUBDIVISION add constraint FKE09042CDFFD48DDD foreign key (PARENT_ID) references SUBDIVISION;
alter table SUBDIVISION add unique (CODE, NAME, COUNTRY);
alter table USERPROFILE add constraint FKF8AB675E9CE4FE3A foreign key (GEOLOCATION_ID) references GEOLOCATION;
