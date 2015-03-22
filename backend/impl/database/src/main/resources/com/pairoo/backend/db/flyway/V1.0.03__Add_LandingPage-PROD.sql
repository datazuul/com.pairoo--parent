drop table if exists LANDINGPAGEACTION;
create table LANDINGPAGEACTION (ID int8 not null, TOKEN varchar(255), ACTIONTYPE varchar(255), USERACCOUNT_ID int8, primary key (ID));
alter table LANDINGPAGEACTION add constraint FKBFBD62BC90690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
