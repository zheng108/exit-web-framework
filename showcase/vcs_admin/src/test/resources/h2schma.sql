alter table TB_DATA_DICTIONARY drop constraint FKE7EA007A162295A6
alter table TB_DICTIONARY_CATEGORY drop constraint FKF06D5F369F71C3FA
alter table TB_GROUP drop constraint FK5F69614EF126EC69
alter table TB_GROUP_RESOURCE drop constraint FK48AB1BDF1385CFA8
alter table TB_GROUP_RESOURCE drop constraint FK48AB1BDFB6BB21EC
alter table TB_GROUP_USER drop constraint FKEAC7DC1C1385CFA8
alter table TB_GROUP_USER drop constraint FKEAC7DC1CCD9EF5CC
alter table TB_RESOURCE drop constraint FKF5B0B6BF521D7150
drop table TB_DATA_DICTIONARY if exists
drop table TB_DICTIONARY_CATEGORY if exists
drop table TB_GROUP if exists
drop table TB_GROUP_RESOURCE if exists
drop table TB_GROUP_USER if exists
drop table TB_RESOURCE if exists
drop table TB_USER if exists
create table TB_DATA_DICTIONARY (id varchar(32) not null unique, name varchar(512) not null, pinYinCode varchar(512), remark text, type varchar(1) not null, value varchar(64) not null, wubiCode varchar(512), FK_CATEGORY_ID char(32) not null, primary key (id))
create table TB_DICTIONARY_CATEGORY (id varchar(32) not null unique, code varchar(128) not null unique, name varchar(256) not null, remark text, FK_PARENT_ID char(32), primary key (id))
create table TB_GROUP (id varchar(32) not null unique, name varchar(64) not null unique, remark text, state integer not null, type varchar(2) not null, FK_PARENT_ID varchar(32), primary key (id))
create table TB_GROUP_RESOURCE (FK_RESOURCE_ID varchar(32) not null, FK_GROUP_ID varchar(32) not null)
create table TB_GROUP_USER (FK_GROUP_ID varchar(32) not null, FK_USER_ID varchar(32) not null)
create table TB_RESOURCE (id varchar(32) not null unique, icon varchar(64), name varchar(64) not null unique, permission varchar(64) unique, remark text, sort integer not null, type varchar(2) not null, value varchar(512), FK_PARENT_ID varchar(32), primary key (id))
create table TB_USER (id varchar(32) not null unique, email varchar(256), password varchar(32) not null, realname varchar(128) not null, state integer not null, username varchar(64) not null unique, primary key (id))
alter table TB_DATA_DICTIONARY add constraint FKE7EA007A162295A6 foreign key (FK_CATEGORY_ID) references TB_DICTIONARY_CATEGORY
alter table TB_DICTIONARY_CATEGORY add constraint FKF06D5F369F71C3FA foreign key (FK_PARENT_ID) references TB_DICTIONARY_CATEGORY
alter table TB_GROUP add constraint FK5F69614EF126EC69 foreign key (FK_PARENT_ID) references TB_GROUP
alter table TB_GROUP_RESOURCE add constraint FK48AB1BDF1385CFA8 foreign key (FK_GROUP_ID) references TB_GROUP
alter table TB_GROUP_RESOURCE add constraint FK48AB1BDFB6BB21EC foreign key (FK_RESOURCE_ID) references TB_RESOURCE
alter table TB_GROUP_USER add constraint FKEAC7DC1C1385CFA8 foreign key (FK_GROUP_ID) references TB_GROUP
alter table TB_GROUP_USER add constraint FKEAC7DC1CCD9EF5CC foreign key (FK_USER_ID) references TB_USER
alter table TB_RESOURCE add constraint FKF5B0B6BF521D7150 foreign key (FK_PARENT_ID) references TB_RESOURCE
