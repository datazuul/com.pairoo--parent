create table NOTIFICATIONSETTINGS (ID bigint not null, ONNEWMESSAGE bit, ONNEWSUGGESTIONS bit, ONVISIT bit, NEWSLETTER bit, WEEKENDSUGGESTIONS bit, WEEKLYSTATISTIC bit, primary key (ID));
alter table USERACCOUNT add column NOTIFICATIONSETTINGS bigint;
alter table USERACCOUNT add unique (NOTIFICATIONSETTINGS);
alter table USERACCOUNT add constraint FKC4F067E263438D1F foreign key (NOTIFICATIONSETTINGS) references NOTIFICATIONSETTINGS;