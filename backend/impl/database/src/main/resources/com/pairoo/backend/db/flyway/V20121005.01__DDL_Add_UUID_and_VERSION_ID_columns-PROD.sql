alter table ADDRESS add column UUID varchar(36);
alter table ADDRESS add column VERSION_ID int4;

alter table APPEARANCE add column UUID varchar(36);
alter table APPEARANCE add column VERSION_ID int4;

/* not in BANKACCOUNT sub-table but in PAYMENTCHANNELS */

alter table BLOCKEDUSER add column UUID varchar(36);
alter table BLOCKEDUSER add column VERSION_ID int4;

alter table CONTACTEVENT add column UUID varchar(36);
alter table CONTACTEVENT add column VERSION_ID int4;

/* not in CREDITCARDACCOUNT sub-table but in PAYMENTCHANNELS */

alter table FAVORITE add column UUID varchar(36);
alter table FAVORITE add column VERSION_ID int4;

alter table GEOAREA add column UUID varchar(36);
alter table GEOAREA add column VERSION_ID int4;

alter table GEOLOCATION add column UUID varchar(36);
alter table GEOLOCATION add column VERSION_ID int4;

alter table IMAGEENTRY add column UUID varchar(36);
alter table IMAGEENTRY add column VERSION_ID int4;

alter table LANDINGPAGEACTION add column UUID varchar(36);
alter table LANDINGPAGEACTION add column VERSION_ID int4;

alter table LIFESTYLE add column UUID varchar(36);
alter table LIFESTYLE add column VERSION_ID int4;

alter table MEMBERSHIP add column UUID varchar(36);
alter table MEMBERSHIP add column VERSION_ID int4;

alter table MESSAGE add column UUID varchar(36);
alter table MESSAGE add column VERSION_ID int4;

alter table NOTIFICATIONSETTINGS add column UUID varchar(36);
alter table NOTIFICATIONSETTINGS add column VERSION_ID int4;

alter table PAYMENTCHANNELS add column UUID varchar(36);
alter table PAYMENTCHANNELS add column VERSION_ID int4;

/* not in PAYONETRANSACTION sub-table but in TRANSACTION */

alter table PERSONALVALUES add column UUID varchar(36);
alter table PERSONALVALUES add column VERSION_ID int4;

alter table PRODUCT add column UUID varchar(36);
alter table PRODUCT add column VERSION_ID int4;

alter table PROMOTION add column UUID varchar(36);
alter table PROMOTION add column VERSION_ID int4;

alter table SEARCHPROFILE add column UUID varchar(36);
alter table SEARCHPROFILE add column VERSION_ID int4;

alter table SUBDIVISION add column UUID varchar(36);
alter table SUBDIVISION add column VERSION_ID int4;

alter table TRANSACTION add column UUID varchar(36);
alter table TRANSACTION add column VERSION_ID int4;

alter table USERS add column UUID varchar(36);
alter table USERS add column VERSION_ID int4;

alter table USERACCOUNT add column VERSION_ID int4;
alter table USERACCOUNT add column UUID varchar(36);

alter table USERPROFILE add column VERSION_ID int4;
alter table USERPROFILE add column UUID varchar(36);

alter table VISIT add column VERSION_ID int4;
alter table VISIT add column UUID varchar(36);

/* not in VOUCHER_PAYMENT sub-table but in PAYMENTCHANNELS */

/* not in WALLETACCOUNT sub-table but in PAYMENTCHANNELS */