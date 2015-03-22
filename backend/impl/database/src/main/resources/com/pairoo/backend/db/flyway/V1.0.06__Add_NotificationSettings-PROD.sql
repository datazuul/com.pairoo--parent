create table NOTIFICATIONSETTINGS (ID int8 not null, ONNEWMESSAGE bool, ONNEWSUGGESTIONS bool, ONVISIT bool, NEWSLETTER bool, WEEKENDSUGGESTIONS bool, WEEKLYSTATISTIC bool, primary key (ID));
alter table USERACCOUNT add column NOTIFICATIONSETTINGS int8 unique;
alter table USERACCOUNT add constraint FKC4F067E263438D1F foreign key (NOTIFICATIONSETTINGS) references NOTIFICATIONSETTINGS;