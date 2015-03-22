create table BLOCKEDUSER (ID bigint not null, TIME_STAMP timestamp, OWNER bigint, TARGET bigint, primary key (ID));
alter table BLOCKEDUSER add constraint FKD2E88B97B424583F foreign key (TARGET) references USERS;
alter table BLOCKEDUSER add constraint FKD2E88B97EEF9D961 foreign key (OWNER) references USERS;