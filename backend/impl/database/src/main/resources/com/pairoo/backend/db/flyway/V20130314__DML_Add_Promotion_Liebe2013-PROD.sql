-- create a promotion
insert into promotion
  (ID, PROMOTION_TYPE, CODE, PRODUCT, VALID_FROM, VALID_TO, TIME_STAMP, UUID, VERSION_ID) values
  ((select max(id)+1 from promotion), 'VOUCHER', 'Liebe2013', 4, '2013-03-01 00:00:00', '2013-12-31 23:59:59', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
update ID_SEQUENCES
  set NEXT_VAL = (select max(ID) + 1 from PROMOTION)
  where SEQUENCE_NAME='com.pairoo.domain.marketing.Promotion';
-- create a LandingPageAction
insert into LANDINGPAGEACTION
  (ID, TOKEN, ACTIONTYPE, USERACCOUNT_ID, UUID, VERSION_ID) values
  ((select max(id)+1 from LANDINGPAGEACTION), 'Liebe2013', 'PROMOTION', null, CURRENT_TIMESTAMP, 0);
update ID_SEQUENCES set NEXT_VAL = (select max(ID) + 1 from LANDINGPAGEACTION) where SEQUENCE_NAME='com.pairoo.domain.LandingPageAction';