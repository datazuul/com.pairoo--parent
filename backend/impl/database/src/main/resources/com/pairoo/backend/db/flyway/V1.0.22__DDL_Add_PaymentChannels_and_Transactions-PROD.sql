alter table MEMBERSHIP drop constraint FKCD0773D69275C37D;
alter table PAYPALACCOUNT drop constraint FKF85B1BBA4FBB85B;
alter table VOUCHER drop constraint FK50F41A8E4FBB85B;

drop table if exists BANKACCOUNT;
drop table if exists CREDITCARDACCOUNT;
drop table if exists PAYMENTCHANNEL;
drop table if exists PAYMENTCHANNELS;
drop table if exists PAYPALACCOUNT;
drop table if exists TRANSACTION;
drop table if exists VOUCHER;
drop table if exists WALLETACCOUNT;

create table BANKACCOUNT (ID int8 not null, BANKCOUNTRY varchar(255), BANKACCOUNT varchar(255), BANKCODE int4, BANKACCOUNTHOLDER varchar(255), primary key (ID));
create table CREDITCARDACCOUNT (ID int8 not null, PSEUDOPAN varchar(255), HOLDERNAME varchar(255), TRUNCATED_PAN varchar(255), VALIDTHRU timestamp, primary key (ID));
create table PAYMENTCHANNELS (ID int8 not null, PAYMENTCHANNELTYPE varchar(255), USERACCOUNT_ID int8, STARTDATE timestamp, ENDDATE timestamp, primary key (ID), unique (USERACCOUNT_ID, STARTDATE));
create table PAYONETRANSACTION (ID int8 not null, SUBACCOUNTID int4, PAYONEID int8, PAYONEDEBITORID int4, ERRORCODE int4, ERRORMESSAGE varchar(255), MERCHANTTRANSACTIONREFERENCE varchar(255), PARAM varchar(255), primary key (ID));
create table TRANSACTION (ID int8 not null, TIME_STAMP timestamp, USERACCOUNT_ID int8, PAYMENTCHANNEL int8, CLEARINGTYPE varchar(255), STATUS varchar(255), AMOUNT int4, CURRENCY varchar(255), NARRATIVETEXT varchar(255), primary key (ID));
create table VOUCHER (ID int8 not null, CODE varchar(255), GENERATEDDATE timestamp, ENCASHEDDATE timestamp, VALIDTHRU timestamp, primary key (ID));
create table WALLETACCOUNT (ID int8 not null, WALLETTYPE varchar(255), primary key (ID));

alter table USERACCOUNT add column PAYMENTCHANNEL int8;
alter table USERACCOUNT add unique (PAYMENTCHANNEL);

alter table BANKACCOUNT add constraint FKEA0CDC516FBBDE43 foreign key (ID) references PAYMENTCHANNELS;
alter table CREDITCARDACCOUNT add constraint FK16BD8C646FBBDE43 foreign key (ID) references PAYMENTCHANNELS;
alter table PAYMENTCHANNELS add constraint FK64608BB690690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table PAYONETRANSACTION add constraint FK2A99D340148B52CE foreign key (ID) references TRANSACTION;
alter table TRANSACTION add constraint FKFFF466BE90690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table TRANSACTION add constraint FKFFF466BEFD35E965 foreign key (PAYMENTCHANNEL) references PAYMENTCHANNELS;
alter table USERACCOUNT add constraint FKC4F067E2FD35E965 foreign key (PAYMENTCHANNEL) references PAYMENTCHANNELS;
alter table VOUCHER add constraint FK50F41A8E6FBBDE43 foreign key (ID) references PAYMENTCHANNELS;
alter table WALLETACCOUNT add constraint FKAD4893F46FBBDE43 foreign key (ID) references PAYMENTCHANNELS;
