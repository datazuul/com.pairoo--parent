create table MEMBERSHIP  (ID int8 not null, ACCEPTEDTERMS bool, STARTDATE timestamp, ENDDATE timestamp, STATUS varchar(255), USERACCOUNT_ID int8, PRODUCT int8, PAYMENTTRANSACTION int8, primary key (ID));

alter table MEMBERSHIP add constraint "uc" unique (USERACCOUNT_ID, STARTDATE, PRODUCT);
alter table MEMBERSHIP add constraint FKCD0773D690690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table MEMBERSHIP add constraint FKCD0773D61A0686CB foreign key (PAYMENTTRANSACTION) references TRANSACTION;
alter table MEMBERSHIP add constraint FKCD0773D665F9901B foreign key (PRODUCT) references PRODUCT;

alter table MEMBERSHIPS drop constraint FKD3E7073D90690217;
alter table MEMBERSHIPS drop constraint FKD3E7073D1A0686CB;
alter table MEMBERSHIPS drop constraint FKD3E7073D65F9901B;

INSERT INTO MEMBERSHIP SELECT * FROM MEMBERSHIPS;
drop table MEMBERSHIPS;