alter table ADDRESS drop constraint FKE66327D4C1F1F768;
alter table APPEARANCE_APPEARANCESTYLES drop constraint FKE50768C16376BDD;
alter table BANKACCOUNT drop constraint FKEA0CDC516FBBDE43;
alter table BLOCKEDUSER drop constraint FKD2E88B97B424583F;
alter table BLOCKEDUSER drop constraint FKD2E88B97EEF9D961;
alter table CONTACTEVENT drop constraint FKD69FCA7A90690217;
alter table CREDITCARDACCOUNT drop constraint FK16BD8C646FBBDE43;
alter table FAVORITE drop constraint FK6D47B99CB424583F;
alter table FAVORITE drop constraint FK6D47B99CEEF9D961;
alter table GEOAREA drop constraint FK2610DB3EC1F1F768;
alter table GEOAREA drop constraint FK2610DB3EDF4495E8;
alter table GEOLOCATION drop constraint FK844CE1E645C7C368;
alter table GEOLOCATION drop constraint FK844CE1E6334F8E24;
alter table IMAGEENTRY drop constraint FKC73CEBF7B24D0097;
alter table LANDINGPAGEACTION drop constraint FKBFBD62BC90690217;
alter table LIFESTYLE_FOODTYPES drop constraint FK981D8711D4687477;
alter table LIFESTYLE_HOBBYTYPES drop constraint FKAC308451D4687477;
alter table LIFESTYLE_HOLIDAYTYPES drop constraint FK8890870BD4687477;
alter table LIFESTYLE_KITCHENTYPES drop constraint FKAB52873FD4687477;
alter table LIFESTYLE_MUSICTYPES drop constraint FK1DEF4FDED4687477;
alter table LIFESTYLE_PETTYPES drop constraint FKCA10EFA4D4687477;
alter table LIFESTYLE_SPORTTYPES drop constraint FK4BE78FEFD4687477;
alter table MEMBERSHIP drop constraint FKCD0773D6AE15FA3B;
alter table MEMBERSHIP drop constraint FKCD0773D690690217;
alter table MEMBERSHIP drop constraint FKCD0773D61A0686CB;
alter table MEMBERSHIP drop constraint FKCD0773D665F9901B;
alter table MESSAGE drop constraint FK63B68BE7B2A600E3;
alter table MESSAGE drop constraint FK63B68BE7B871D09D;
alter table PAYMENTCHANNELS drop constraint FK64608BB690690217;
alter table PAYONETRANSACTION drop constraint FK2A99D340148B52CE;
alter table PROMOTION drop constraint FK76750C8365F9901B;
alter table PROMOTION drop constraint FK76750C834EC0CFA1;
alter table SEARCHPROFILE drop constraint FK928C0EE141247F3A;
alter table SEARCHPROFILE_APPEARANCESTYLES drop constraint FK1B112BE4D4CB277;
alter table SEARCHPROFILE_BODYTYPES drop constraint FKCB868819D4CB277;
alter table SEARCHPROFILE_EDUCATIONTYPES drop constraint FK261FEEFD4CB277;
alter table SEARCHPROFILE_ETHNICITIES drop constraint FKF6BBE8DFD4CB277;
alter table SEARCHPROFILE_EYECOLORS drop constraint FKA62ED2E3D4CB277;
alter table SEARCHPROFILE_FAMILYSTATUSTYPES drop constraint FK19888FE5D4CB277;
alter table SEARCHPROFILE_HAIRCOLORS drop constraint FKBACE57F0D4CB277;
alter table SEARCHPROFILE_HOBBYTYPES drop constraint FKD6D77A05D4CB277;
alter table SEARCHPROFILE_LANGUAGES drop constraint FKDA2C8C1DD4CB277;
alter table SEARCHPROFILE_RELIGIONS drop constraint FK8C278D64D4CB277;
alter table SEARCHPROFILE_SPORTTYPES drop constraint FK768E85A3D4CB277;
alter table SUBDIVISION drop constraint FKE09042CD24E1870B;
alter table TRANSACTION drop constraint FKFFF466BE90690217;
alter table TRANSACTION drop constraint FKFFF466BEFD35E965;
alter table USERACCOUNT drop constraint FKC4F067E263438D1F;
alter table USERACCOUNT drop constraint FKC4F067E2DFE0D4BD;
alter table USERPROFILE drop constraint FKF8AB675E272B4347;
alter table USERPROFILE drop constraint FKF8AB675E8E020E07;
alter table USERPROFILE drop constraint FKF8AB675EC1F1F768;
alter table USERPROFILE drop constraint FKF8AB675EF2217D6B;
alter table USERPROFILE_LANGUAGES drop constraint FKB6A92C5AB24D0097;
alter table USERS drop constraint FK4D495E8B636CE99;
alter table USERS drop constraint FK4D495E820D2E25;
alter table USERS drop constraint FK4D495E81DBE1A5F;
alter table USERS drop constraint FK4D495E84EC0CFA1;
alter table VISIT drop constraint FK4DE552B4781B1C;
alter table VISIT drop constraint FK4DE552B9CAD60A3;
alter table VOUCHER_PAYMENT drop constraint FKD6A711356FBBDE43;
alter table WALLETACCOUNT drop constraint FKAD4893F46FBBDE43;
drop table ADDRESS if exists;
drop table APPEARANCE if exists;
drop table APPEARANCE_APPEARANCESTYLES if exists;
drop table BANKACCOUNT if exists;
drop table BLOCKEDUSER if exists;
drop table CONTACTEVENT if exists;
drop table CREDITCARDACCOUNT if exists;
drop table FAVORITE if exists;
drop table GEOAREA if exists;
drop table GEOLOCATION if exists;
drop table IMAGEENTRY if exists;
drop table LANDINGPAGEACTION if exists;
drop table LIFESTYLE if exists;
drop table LIFESTYLE_FOODTYPES if exists;
drop table LIFESTYLE_HOBBYTYPES if exists;
drop table LIFESTYLE_HOLIDAYTYPES if exists;
drop table LIFESTYLE_KITCHENTYPES if exists;
drop table LIFESTYLE_MUSICTYPES if exists;
drop table LIFESTYLE_PETTYPES if exists;
drop table LIFESTYLE_SPORTTYPES if exists;
drop table MEMBERSHIP if exists;
drop table MESSAGE if exists;
drop table NOTIFICATIONSETTINGS if exists;
drop table PAYMENTCHANNELS if exists;
drop table PAYONETRANSACTION if exists;
drop table PERSONALVALUES if exists;
drop table PRODUCT if exists;
drop table PROMOTION if exists;
drop table SEARCHPROFILE if exists;
drop table SEARCHPROFILE_APPEARANCESTYLES if exists;
drop table SEARCHPROFILE_BODYTYPES if exists;
drop table SEARCHPROFILE_EDUCATIONTYPES if exists;
drop table SEARCHPROFILE_ETHNICITIES if exists;
drop table SEARCHPROFILE_EYECOLORS if exists;
drop table SEARCHPROFILE_FAMILYSTATUSTYPES if exists;
drop table SEARCHPROFILE_HAIRCOLORS if exists;
drop table SEARCHPROFILE_HOBBYTYPES if exists;
drop table SEARCHPROFILE_LANGUAGES if exists;
drop table SEARCHPROFILE_RELIGIONS if exists;
drop table SEARCHPROFILE_SPORTTYPES if exists;
drop table SUBDIVISION if exists;
drop table TRANSACTION if exists;
drop table USERACCOUNT if exists;
drop table USERPROFILE if exists;
drop table USERPROFILE_LANGUAGES if exists;
drop table USERS if exists;
drop table VISIT if exists;
drop table VOUCHER_PAYMENT if exists;
drop table WALLETACCOUNT if exists;
drop table ID_SEQUENCES if exists;
create table ADDRESS (ID bigint not null, ZIPCODE varchar(255), STREET varchar(255), HOUSENR varchar(255), CITY varchar(255), COUNTRY varchar(255), GEOLOCATION_ID bigint, primary key (ID));
create table APPEARANCE (ID bigint not null, HEIGHT integer, WEIGHT integer, BODYTYPE varchar(255), EYECOLOR varchar(255), HAIRCOLOR varchar(255), ETHNICITY varchar(255), primary key (ID));
create table APPEARANCE_APPEARANCESTYLES (APPEARANCE_ID bigint not null, APPEARANCESTYLE varchar(255));
create table BANKACCOUNT (ID bigint not null, BANKCOUNTRY varchar(255), BANKACCOUNT varchar(255), BANKCODE integer, BANKACCOUNTHOLDER varchar(255), primary key (ID));
create table BLOCKEDUSER (ID bigint not null, TIME_STAMP timestamp, OWNER bigint, TARGET bigint, primary key (ID));
create table CONTACTEVENT (ID bigint not null, TIMESTAMP timestamp, USERACCOUNT_ID bigint, CONTACTEVENTTYPE varchar(255), primary key (ID));
create table CREDITCARDACCOUNT (ID bigint not null, PSEUDOPAN varchar(255), HOLDERNAME varchar(255), TRUNCATED_PAN varchar(255), VALIDTHRU timestamp, primary key (ID));
create table FAVORITE (ID bigint not null, TIME_STAMP timestamp, OWNER bigint, TARGET bigint, primary key (ID));
create table GEOAREA (ID bigint not null, CONTINENT varchar(255), COUNTRY varchar(255), GEOLOCATION_ID bigint, MAXDISTANCE varchar(255), SUBDIVISION_ID bigint, ZIPCODE_START varchar(255), primary key (ID));
create table GEOLOCATION (ID bigint not null, NAME varchar(255), ZIPCODE varchar(255), LATITUDE double, LONGITUDE double, POPULATION integer, COUNTRY varchar(255), SUBDIVISION_BIGGEST_ID bigint, SUBDIVISION_SMALLEST_ID bigint, CONTINENT varchar(255), primary key (ID));
create table IMAGEENTRY (ID bigint not null, PROFILEIMAGE bit, VISIBLE bit, CLIENTFILENAME varchar(255), LASTMODIFIED timestamp, MEDIATYPE varchar(255), HEIGHT integer, WIDTH integer, REPOSITORYID varchar(255), USERPROFILE_ID bigint, idx integer, primary key (ID));
create table LANDINGPAGEACTION (ID bigint not null, TOKEN varchar(255), ACTIONTYPE varchar(255), USERACCOUNT_ID bigint, primary key (ID));
create table LIFESTYLE (ID bigint not null, DRINKTYPE varchar(255), SMOKETYPE varchar(255), OCCUPATIONTYPE varchar(255), PROFESSIONTYPE varchar(255), HOMETYPE varchar(255), LIVINGSITUATION varchar(255), PLAYINSTRUMENT bit, SPORTSACTIVITYTYPE varchar(255), HOLIDAYPLANNINGTYPE varchar(255), FITNESSTYPE varchar(255), primary key (ID));
create table LIFESTYLE_FOODTYPES (LIFESTYLE_ID bigint not null, FOODTYPE varchar(255));
create table LIFESTYLE_HOBBYTYPES (LIFESTYLE_ID bigint not null, HOBBYTYPE varchar(255));
create table LIFESTYLE_HOLIDAYTYPES (LIFESTYLE_ID bigint not null, HOLIDAYTYPE varchar(255));
create table LIFESTYLE_KITCHENTYPES (LIFESTYLE_ID bigint not null, KITCHENTYPE varchar(255));
create table LIFESTYLE_MUSICTYPES (LIFESTYLE_ID bigint not null, MUSICTYPE varchar(255));
create table LIFESTYLE_PETTYPES (LIFESTYLE_ID bigint not null, PETTYPE varchar(255));
create table LIFESTYLE_SPORTTYPES (LIFESTYLE_ID bigint not null, SPORTTYPE varchar(255));
create table MEMBERSHIP (ID bigint not null, USERACCOUNT_ID bigint, STARTDATE timestamp, ENDDATE timestamp, ACCEPTEDTERMS bit, STATUS varchar(255), PAYMENTTRANSACTION bigint, PRODUCT bigint, PROMOTION bigint, primary key (ID), unique (USERACCOUNT_ID, STARTDATE));
create table MESSAGE (ID bigint not null, MESSAGETYPE varchar(255), MESSAGESTATE_RECEIVER varchar(255), MESSAGESTATE_SENDER varchar(255), TIME_STAMP timestamp, RECEIVER bigint, SENDER bigint, SUBJECT varchar(255), TEXT varchar(4000), primary key (ID));
create table NOTIFICATIONSETTINGS (ID bigint not null, ONNEWMESSAGE bit, ONNEWSUGGESTIONS bit, ONVISIT bit, NEWSLETTER bit, WEEKENDSUGGESTIONS bit, WEEKLYSTATISTIC bit, primary key (ID));
create table PAYMENTCHANNELS (ID bigint not null, PAYMENTCHANNELTYPE varchar(255), USERACCOUNT_ID bigint, STARTDATE timestamp, ENDDATE timestamp, primary key (ID), unique (USERACCOUNT_ID, STARTDATE));
create table PAYONETRANSACTION (ID bigint not null, CUSTOMERMESSAGE varchar(255), ERRORCODE integer, ERRORMESSAGE varchar(255), MERCHANTTRANSACTIONREFERENCE varchar(255), PARAM varchar(255), PAYONEDEBITORID integer, PAYONEID bigint, REDIRECTURL bigint, SUBACCOUNTID integer, primary key (ID));
create table PERSONALVALUES (ID bigint not null, SHOWINGEMOTIONS varchar(255), ROMANCE varchar(255), TENDERNESS varchar(255), LONGRELATIONSHIP varchar(255), SHORTRELATIONSHIP varchar(255), FREEDOM varchar(255), SEXUALITY varchar(255), DIFFERENTPARTNERS varchar(255), FAITHFULNESS varchar(255), RESPECT varchar(255), HONESTY varchar(255), SECURITY varchar(255), CONFIDENCE varchar(255), CHARM varchar(255), primary key (ID));
create table PRODUCT (ID bigint not null, STARTDATE timestamp, ENDDATE timestamp, DURATION varchar(255), MONTHLYRATE float, ABO bit, ROLE varchar(255), primary key (ID));
create table PROMOTION (ID bigint not null, PROMOTION_TYPE varchar(255), CODE varchar(255), USERACCOUNT bigint, PRODUCT bigint, VALID_FROM timestamp, VALID_TO timestamp, TIME_STAMP timestamp, primary key (ID));
create table SEARCHPROFILE (ID bigint not null, PARTNERTYPE varchar(255), PROFILE_PICTURE_TYPE varchar(255), MINAGE integer, MAXAGE integer, GEOAREA_ID bigint, NUMBER_OF_KIDS_TYPE varchar(255), WANT_MORE_CHILDREN_TYPE varchar(255), PARTNERSHIPTYPE varchar(255), MOTHERLANGUAGE varchar(255), HOUSEHOLDTYPE varchar(255), MINHEIGHTCM integer, MAXHEIGHTCM integer, SMOKETYPE varchar(255), INCOMETYPE varchar(255), OCCUPATIONTYPE varchar(255), primary key (ID));
create table SEARCHPROFILE_APPEARANCESTYLES (SEARCHPROFILE_ID bigint not null, APPEARANCESTYLE varchar(255));
create table SEARCHPROFILE_BODYTYPES (SEARCHPROFILE_ID bigint not null, BODYTYPE varchar(255));
create table SEARCHPROFILE_EDUCATIONTYPES (SEARCHPROFILE_ID bigint not null, EDUCATIONTYPE varchar(255));
create table SEARCHPROFILE_ETHNICITIES (SEARCHPROFILE_ID bigint not null, ETHNICITY varchar(255));
create table SEARCHPROFILE_EYECOLORS (SEARCHPROFILE_ID bigint not null, EYECOLOR varchar(255));
create table SEARCHPROFILE_FAMILYSTATUSTYPES (SEARCHPROFILE_ID bigint not null, FAMILYSTATUSTYPE varchar(255));
create table SEARCHPROFILE_HAIRCOLORS (SEARCHPROFILE_ID bigint not null, HAIRCOLOR varchar(255));
create table SEARCHPROFILE_HOBBYTYPES (SEARCHPROFILE_ID bigint not null, HOBBYTYPE varchar(255));
create table SEARCHPROFILE_LANGUAGES (SEARCHPROFILE_ID bigint not null, LANGUAGE varchar(255));
create table SEARCHPROFILE_RELIGIONS (SEARCHPROFILE_ID bigint not null, RELIGION varchar(255));
create table SEARCHPROFILE_SPORTTYPES (SEARCHPROFILE_ID bigint not null, SPORTTYPE varchar(255));
create table SUBDIVISION (ID bigint not null, CODE varchar(255), NAME varchar(255), COUNTRY varchar(255), PARENT_ID bigint, primary key (ID), unique (CODE, NAME, COUNTRY));
create table TRANSACTION (ID bigint not null, TIME_STAMP timestamp, USERACCOUNT_ID bigint, PAYMENTCHANNEL bigint, CLEARINGTYPE varchar(255), STATUS varchar(255), AMOUNT integer, CURRENCY varchar(255), NARRATIVETEXT varchar(255), primary key (ID));
create table USERACCOUNT (ID bigint not null, ROLES varchar(255), LASTLOGIN timestamp, NOTIFICATIONSETTINGS bigint, ONLINE bit, PASSWORD varchar(255) not null, PASSWORD_SALT varchar(255), PREFERRED_LANGUAGE varchar(255), PREMIUM_END_DATE timestamp, USERNAME varchar(255), USER_ID bigint, primary key (ID), unique (NOTIFICATIONSETTINGS));
create table USERPROFILE (ID bigint not null, APPEARANCE bigint, BIRTHDATE timestamp, EDUCATION varchar(255), FAMILYSTATUS varchar(255), GEOLOCATION_ID bigint, HOUSEHOLDTYPE varchar(255), INCOMETYPE varchar(255), LIFESTYLE bigint, MOTHERLANGUAGE varchar(255), MOTTO varchar(255), NUMBEROFKIDS varchar(255), OCCUPATIONTYPE varchar(255), PARTNERTYPE varchar(255), PERSONALVALUES bigint, PROFESSION varchar(255), RELIGION varchar(255), WANT_MORE_CHILDREN_TYPE varchar(255), primary key (ID));
create table USERPROFILE_LANGUAGES (USERPROFILE_ID bigint not null, LANGUAGE varchar(255));
create table USERS (ID bigint not null, EMAIL varchar(256) not null, FIRSTNAME varchar(255), LASTNAME varchar(255), ADDRESS bigint, SEARCHPROFILE bigint, USERPROFILE bigint, USERACCOUNT bigint, primary key (ID));
create table VISIT (ID bigint not null, TIME_STAMP timestamp, VISITEDUSER bigint, VISITOR bigint, primary key (ID));
create table VOUCHER_PAYMENT (ID bigint not null, PROMOTION_CODE varchar(255), primary key (ID));
create table WALLETACCOUNT (ID bigint not null, WALLETTYPE varchar(255), primary key (ID));
alter table ADDRESS add constraint FKE66327D4C1F1F768 foreign key (GEOLOCATION_ID) references GEOLOCATION;
alter table APPEARANCE_APPEARANCESTYLES add constraint FKE50768C16376BDD foreign key (APPEARANCE_ID) references APPEARANCE;
alter table BANKACCOUNT add constraint FKEA0CDC516FBBDE43 foreign key (ID) references PAYMENTCHANNELS;
alter table BLOCKEDUSER add constraint FKD2E88B97B424583F foreign key (TARGET) references USERS;
alter table BLOCKEDUSER add constraint FKD2E88B97EEF9D961 foreign key (OWNER) references USERS;
alter table CONTACTEVENT add constraint FKD69FCA7A90690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table CREDITCARDACCOUNT add constraint FK16BD8C646FBBDE43 foreign key (ID) references PAYMENTCHANNELS;
alter table FAVORITE add constraint FK6D47B99CB424583F foreign key (TARGET) references USERS;
alter table FAVORITE add constraint FK6D47B99CEEF9D961 foreign key (OWNER) references USERS;
alter table GEOAREA add constraint FK2610DB3EC1F1F768 foreign key (GEOLOCATION_ID) references GEOLOCATION;
alter table GEOAREA add constraint FK2610DB3EDF4495E8 foreign key (SUBDIVISION_ID) references SUBDIVISION;
alter table GEOLOCATION add constraint FK844CE1E645C7C368 foreign key (SUBDIVISION_BIGGEST_ID) references SUBDIVISION;
alter table GEOLOCATION add constraint FK844CE1E6334F8E24 foreign key (SUBDIVISION_SMALLEST_ID) references SUBDIVISION;
alter table IMAGEENTRY add constraint FKC73CEBF7B24D0097 foreign key (USERPROFILE_ID) references USERPROFILE;
alter table LANDINGPAGEACTION add constraint FKBFBD62BC90690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table LIFESTYLE_FOODTYPES add constraint FK981D8711D4687477 foreign key (LIFESTYLE_ID) references LIFESTYLE;
alter table LIFESTYLE_HOBBYTYPES add constraint FKAC308451D4687477 foreign key (LIFESTYLE_ID) references LIFESTYLE;
alter table LIFESTYLE_HOLIDAYTYPES add constraint FK8890870BD4687477 foreign key (LIFESTYLE_ID) references LIFESTYLE;
alter table LIFESTYLE_KITCHENTYPES add constraint FKAB52873FD4687477 foreign key (LIFESTYLE_ID) references LIFESTYLE;
alter table LIFESTYLE_MUSICTYPES add constraint FK1DEF4FDED4687477 foreign key (LIFESTYLE_ID) references LIFESTYLE;
alter table LIFESTYLE_PETTYPES add constraint FKCA10EFA4D4687477 foreign key (LIFESTYLE_ID) references LIFESTYLE;
alter table LIFESTYLE_SPORTTYPES add constraint FK4BE78FEFD4687477 foreign key (LIFESTYLE_ID) references LIFESTYLE;
alter table MEMBERSHIP add constraint FKCD0773D6AE15FA3B foreign key (PROMOTION) references PROMOTION;
alter table MEMBERSHIP add constraint FKCD0773D690690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table MEMBERSHIP add constraint FKCD0773D61A0686CB foreign key (PAYMENTTRANSACTION) references TRANSACTION;
alter table MEMBERSHIP add constraint FKCD0773D665F9901B foreign key (PRODUCT) references PRODUCT;
alter table MESSAGE add constraint FK63B68BE7B2A600E3 foreign key (SENDER) references USERS;
alter table MESSAGE add constraint FK63B68BE7B871D09D foreign key (RECEIVER) references USERS;
alter table PAYMENTCHANNELS add constraint FK64608BB690690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table PAYONETRANSACTION add constraint FK2A99D340148B52CE foreign key (ID) references TRANSACTION;
alter table PROMOTION add constraint FK76750C8365F9901B foreign key (PRODUCT) references PRODUCT;
alter table PROMOTION add constraint FK76750C834EC0CFA1 foreign key (USERACCOUNT) references USERACCOUNT;
alter table SEARCHPROFILE add constraint FK928C0EE141247F3A foreign key (GEOAREA_ID) references GEOAREA;
alter table SEARCHPROFILE_APPEARANCESTYLES add constraint FK1B112BE4D4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_BODYTYPES add constraint FKCB868819D4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_EDUCATIONTYPES add constraint FK261FEEFD4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_ETHNICITIES add constraint FKF6BBE8DFD4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_EYECOLORS add constraint FKA62ED2E3D4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_FAMILYSTATUSTYPES add constraint FK19888FE5D4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_HAIRCOLORS add constraint FKBACE57F0D4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_HOBBYTYPES add constraint FKD6D77A05D4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_LANGUAGES add constraint FKDA2C8C1DD4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_RELIGIONS add constraint FK8C278D64D4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SEARCHPROFILE_SPORTTYPES add constraint FK768E85A3D4CB277 foreign key (SEARCHPROFILE_ID) references SEARCHPROFILE;
alter table SUBDIVISION add constraint FKE09042CD24E1870B foreign key (PARENT_ID) references SUBDIVISION;
alter table TRANSACTION add constraint FKFFF466BE90690217 foreign key (USERACCOUNT_ID) references USERACCOUNT;
alter table TRANSACTION add constraint FKFFF466BEFD35E965 foreign key (PAYMENTCHANNEL) references PAYMENTCHANNELS;
alter table USERACCOUNT add constraint FKC4F067E263438D1F foreign key (NOTIFICATIONSETTINGS) references NOTIFICATIONSETTINGS;
alter table USERACCOUNT add constraint FKC4F067E2DFE0D4BD foreign key (USER_ID) references USERS;
alter table USERPROFILE add constraint FKF8AB675E272B4347 foreign key (LIFESTYLE) references LIFESTYLE;
alter table USERPROFILE add constraint FKF8AB675E8E020E07 foreign key (PERSONALVALUES) references PERSONALVALUES;
alter table USERPROFILE add constraint FKF8AB675EC1F1F768 foreign key (GEOLOCATION_ID) references GEOLOCATION;
alter table USERPROFILE add constraint FKF8AB675EF2217D6B foreign key (APPEARANCE) references APPEARANCE;
alter table USERPROFILE_LANGUAGES add constraint FKB6A92C5AB24D0097 foreign key (USERPROFILE_ID) references USERPROFILE;
alter table USERS add constraint FK4D495E8B636CE99 foreign key (USERPROFILE) references USERPROFILE;
alter table USERS add constraint FK4D495E820D2E25 foreign key (ADDRESS) references ADDRESS;
alter table USERS add constraint FK4D495E81DBE1A5F foreign key (SEARCHPROFILE) references SEARCHPROFILE;
alter table USERS add constraint FK4D495E84EC0CFA1 foreign key (USERACCOUNT) references USERACCOUNT;
alter table VISIT add constraint FK4DE552B4781B1C foreign key (VISITOR) references USERS;
alter table VISIT add constraint FK4DE552B9CAD60A3 foreign key (VISITEDUSER) references USERS;
alter table VOUCHER_PAYMENT add constraint FKD6A711356FBBDE43 foreign key (ID) references PAYMENTCHANNELS;
alter table WALLETACCOUNT add constraint FKAD4893F46FBBDE43 foreign key (ID) references PAYMENTCHANNELS;
create table ID_SEQUENCES ( sequence_name varchar(255) not null ,  next_val bigint, primary key ( sequence_name ) ) ;
