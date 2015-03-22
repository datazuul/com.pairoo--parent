create table MEMBERSHIPS (ID bigint not null, USERACCOUNT_ID bigint, ACCEPTEDTERMS bit, STARTDATE timestamp, ENDDATE timestamp, DURATIONTYPE varchar(255), STATUS varchar(255), TYPE varchar(255), PAID bit, BILLSENT timestamp, PAID_UNTIL timestamp, AMOUNT float, PAYMENTCHANNEL bigint, primary key (ID), unique (USERACCOUNT_ID, STARTDATE));

alter table USERACCOUNT add column MEMBERSHIP bigint;
alter table USERACCOUNT add unique (MEMBERSHIP);

INSERT INTO MEMBERSHIPS SELECT * FROM MEMBERSHIP;
drop table membership;

alter table USERACCOUNT add constraint FKC4F067E27C2D144F foreign key (MEMBERSHIP) references MEMBERSHIPS;

alter table MEMBERSHIPS drop column AMOUNT;
alter table MEMBERSHIPS drop column BILLSENT;
alter table MEMBERSHIPS drop column DURATIONTYPE;
alter table MEMBERSHIPS drop column PAID;
alter table MEMBERSHIPS drop column PAID_UNTIL;
alter table MEMBERSHIPS drop column PAYMENTCHANNEL;
alter table MEMBERSHIPS drop column TYPE;

alter table MEMBERSHIPS add column PRODUCT bigint;
alter table MEMBERSHIPS add column PAYMENTTRANSACTION bigint;

create table PRODUCT (ID bigint not null, STARTDATE timestamp, ENDDATE timestamp, DURATION varchar(255), MONTHLYRATE float, ABO bit, ROLE varchar(255), primary key (ID));
alter table MEMBERSHIPS add constraint FKD3E7073D90690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table MEMBERSHIPS add constraint FKD3E7073D1A0686CB foreign key (PAYMENTTRANSACTION) references TRANSACTION;
alter table MEMBERSHIPS add constraint FKD3E7073D65F9901B foreign key (PRODUCT) references PRODUCT;