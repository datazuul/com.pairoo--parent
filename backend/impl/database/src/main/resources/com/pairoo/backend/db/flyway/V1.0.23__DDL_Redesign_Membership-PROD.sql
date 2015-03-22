create table MEMBERSHIPS (ID int8 not null, ACCEPTEDTERMS bool, STARTDATE timestamp, ENDDATE timestamp, DURATIONTYPE varchar(255), STATUS varchar(255), TYPE varchar(255), PAID bool, BILLSENT timestamp, AMOUNT float4, PAYMENTCHANNEL int8, USERACCOUNT_ID int8, PAID_UNTIL timestamp, primary key (ID), unique (USERACCOUNT_ID, STARTDATE));

alter table USERACCOUNT add column MEMBERSHIP int8;
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

alter table MEMBERSHIPS add column PRODUCT int8;
alter table MEMBERSHIPS add column PAYMENTTRANSACTION int8;

create table PRODUCT (ID int8 not null, STARTDATE timestamp, ENDDATE timestamp, DURATION varchar(255), MONTHLYRATE float4, ABO bool, ROLE varchar(255), primary key (ID));
alter table MEMBERSHIPS add constraint FKD3E7073D90690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table MEMBERSHIPS add constraint FKD3E7073D1A0686CB foreign key (PAYMENTTRANSACTION) references TRANSACTION;
alter table MEMBERSHIPS add constraint FKD3E7073D65F9901B foreign key (PRODUCT) references PRODUCT;

update useraccount set membership=memberships.id from memberships where memberships.useraccount_id=useraccount.id;