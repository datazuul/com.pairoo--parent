alter table USERACCOUNT drop constraint FKC4F067E27C2D144F;
alter table USERACCOUNT drop column MEMBERSHIP;

alter table USERACCOUNT drop constraint FKC4F067E2FD35E965;
alter table USERACCOUNT drop column PAYMENTCHANNEL;

alter table USERACCOUNT add column PREMIUM_END_DATE timestamp;