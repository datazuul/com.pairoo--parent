create table SEARCHPROFILE_LANGUAGES (SEARCHPROFILE_ID int8 not null, LANGUAGE varchar(255));
alter table SEARCHPROFILE_LANGUAGES add constraint FKDA2C8C1DD4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;