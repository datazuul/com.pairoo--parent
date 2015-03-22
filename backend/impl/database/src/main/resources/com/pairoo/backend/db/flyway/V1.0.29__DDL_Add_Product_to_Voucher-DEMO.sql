alter table VOUCHER add column PRODUCT bigint;
alter table VOUCHER add constraint FK50F41A8E65F9901B foreign key (PRODUCT) references PRODUCT;