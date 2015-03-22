drop table LANDINGPAGEACTION if exists;
create table LANDINGPAGEACTION (ID bigint not null, TOKEN varchar(255), ACTIONTYPE varchar(255), USERACCOUNT_ID bigint, primary key (ID));
alter table LANDINGPAGEACTION add constraint FKBFBD62BC90690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
