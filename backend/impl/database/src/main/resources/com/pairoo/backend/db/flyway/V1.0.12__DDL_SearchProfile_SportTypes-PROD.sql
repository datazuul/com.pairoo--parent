create table SEARCHPROFILE_SPORTTYPES (SEARCHPROFILE_ID int8 not null, SPORTTYPE varchar(255));
alter table SEARCHPROFILE_SPORTTYPES add constraint FK768E85A3D4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;