alter table GEOLOCATION drop column CITY_ID;
alter table GEOLOCATION drop column SUBDIVISION_ID;

alter table SEARCHPROFILE drop column GEOLOCATION;
alter table SEARCHPROFILE drop column MAXDISTANCE;

alter table SUBDIVISION drop column COUNTRYCODE;

alter table USERPROFILE drop column GEOLOCATION;

drop table  if exists CITY;
