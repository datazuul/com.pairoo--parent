-- alter table USERACCOUNT drop column USER_ID;
alter table USERACCOUNT add column USER_ID bigint;
update USERACCOUNT set USER_ID=ID;

-- alter table USERS drop column USERACCOUNT;
alter table USERS add column USERACCOUNT bigint;
update USERS set USERACCOUNT=ID;