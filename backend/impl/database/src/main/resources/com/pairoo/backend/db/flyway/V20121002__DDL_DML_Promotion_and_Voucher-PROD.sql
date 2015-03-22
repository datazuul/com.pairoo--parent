create table PROMOTION (ID int8 not null, PROMOTION_TYPE varchar(255), CODE varchar(255), USERACCOUNT int8, USED bool, PRODUCT int8, VALID_FROM timestamp, VALID_TO timestamp, TIME_STAMP timestamp, primary key (ID));
alter table PROMOTION add constraint FK76750C8365F9901B foreign key (PRODUCT) references PRODUCT;
alter table PROMOTION add constraint FK76750C834EC0CFA1 foreign key (USERACCOUNT) references USERACCOUNT;
/*
 * Migration von Daten aus VOUCHER nach PROMOTION:
 * alle Daten kopieren mit folgendem Neu-mapping:
 * ID → ID
 * CODE → CODE
 * PAYMENTCHANNELS.USERACCOUNT → USERACCOUNT
 * PRODUCT → PRODUCT
 * GENERATEDDATE → VALID_FROM
 * VALIDTHRU → VALID_TO
 * PROMOTION_TYPE = “VOUCHER”
 * GENERATEDDATE → TIME_STAMP
 * ENCASHEDDATE → USED = true
 */
insert into PROMOTION (ID, CODE, PRODUCT, TIME_STAMP, VALID_FROM, VALID_TO) select ID, CODE, PRODUCT, GENERATEDDATE, GENERATEDDATE, VALIDTHRU from VOUCHER; 
update PROMOTION set PROMOTION_TYPE='VOUCHER';
update PROMOTION set USED=true where ID in (select ID from VOUCHER where ENCASHEDDATE != null);
update PROMOTION set USERACCOUNT = (select USERACCOUNT from PAYMENTCHANNELS where PAYMENTCHANNELS.ID=PROMOTION.ID);
/*
 * max-ID + 1 wird als Startwert in die ID_SEQUENCES Tabelle eingetragen
 */
insert into ID_SEQUENCES (SEQUENCE_NAME, NEXT_VAL) values ('com.pairoo.domain.marketing.Promotion', 1);
update ID_SEQUENCES set NEXT_VAL = (select max(ID) + 1 from VOUCHER) where SEQUENCE_NAME='com.pairoo.domain.marketing.Promotion';
update ID_SEQUENCES set NEXT_VAL = 1 where SEQUENCE_NAME='com.pairoo.domain.marketing.Promotion' and (NEXT_VAL is null or NEXT_VAL<1);

/*
 * Die Tabelle VOUCHERPAYMENT wird neu erstellt
 * ergänzt/erweitert Tabelle PaymentChannel (joined subclass)
 */
create table VOUCHER_PAYMENT (ID int8 not null, PROMOTION_CODE varchar(255), primary key (ID));
alter table VOUCHER_PAYMENT add constraint FKD6A711356FBBDE43 foreign key (ID) references PAYMENTCHANNELS;
/*
 * alle Einträge aus VOUCHER, bei denen ein encashed date vorhanden ist
 * (bei denen also eine Transaktion stattgefunden hat),
 * werden mit bestehender ID und CODE nach ID und PROMOTION_CODE übertragen
 */
insert into VOUCHER_PAYMENT (ID, PROMOTION_CODE) select ID, CODE from VOUCHER where ENCASHEDDATE != null;
/*
 * max-ID + 1 wird als Startwert in die ID_SEQUENCES Tabelle eingetragen
 */
insert into ID_SEQUENCES (SEQUENCE_NAME, NEXT_VAL) values ('com.pairoo.domain.payment.VoucherPayment', 1);
update ID_SEQUENCES set NEXT_VAL = (select max(ID) + 1 from VOUCHER_PAYMENT) where SEQUENCE_NAME='com.pairoo.domain.payment.VoucherPayment';
update ID_SEQUENCES set NEXT_VAL = 1 where SEQUENCE_NAME='com.pairoo.domain.payment.VoucherPayment' and (NEXT_VAL is null or NEXT_VAL<1);

alter table MEMBERSHIP add column PROMOTION int8;
alter table MEMBERSHIP add constraint FKCD0773D6AE15FA3B foreign key (PROMOTION) references PROMOTION;

alter table VOUCHER drop constraint FK50F41A8E6FBBDE43;
drop table VOUCHER;