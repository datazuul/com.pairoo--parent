alter table USERPROFILE add column WANT_MORE_CHILDREN_TYPE varchar(255);
update USERPROFILE set WANT_MORE_CHILDREN_TYPE='DONT_SAY';