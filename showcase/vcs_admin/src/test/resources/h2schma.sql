alter table tb_data_dictionary drop constraint FKB034107A704DF8AD
alter table tb_dictionary_category drop constraint FK181DDF36F99D2701
alter table tb_group drop constraint FKFA285D6EE76F3D70
alter table tb_group_resource drop constraint FK898FD3BF9CE20AF
alter table tb_group_resource drop constraint FK898FD3BFE0485F85
alter table tb_group_user drop constraint FK92B07BFC9CE20AF
alter table tb_group_user drop constraint FK92B07BFCE614E7E5
alter table tb_resource drop constraint FKECCF42BF7BAAAEE9
drop table tb_data_dictionary if exists
drop table tb_dictionary_category if exists
drop table tb_group if exists
drop table tb_group_resource if exists
drop table tb_group_user if exists
drop table tb_resource if exists
drop table tb_user if exists
create table tb_data_dictionary (id char(32) not null, name varchar(512) not null, pin_yin_code varchar(512), remark text, type varchar(1) not null, value varchar(64) not null, wubi_code varchar(512), fk_category_id char(32) not null, primary key (id))
create table tb_dictionary_category (id char(32) not null, code varchar(128) not null unique, name varchar(256) not null, remark text, fk_parent_id char(32), primary key (id))
create table tb_group (id char(32) not null, name varchar(64) not null unique, remark text, state integer not null, type varchar(2) not null, fk_parent_id char(32), primary key (id))
create table tb_group_resource (fk_resource_id char(32) not null, fk_group_id char(32) not null)
create table tb_group_user (fk_group_id char(32) not null, fk_user_id char(32) not null)
create table tb_resource (id char(32) not null, icon varchar(64), name varchar(64) not null unique, permission varchar(64) unique, remark text, sort integer not null, type varchar(2) not null, value varchar(512), fk_parent_id char(32), primary key (id))
create table tb_user (id char(32) not null, email varchar(256), password char(32) not null, realname varchar(128) not null, state integer not null, username varchar(64) not null unique, primary key (id))
alter table tb_data_dictionary add constraint FKB034107A704DF8AD foreign key (fk_category_id) references tb_dictionary_category
alter table tb_dictionary_category add constraint FK181DDF36F99D2701 foreign key (fk_parent_id) references tb_dictionary_category
alter table tb_group add constraint FKFA285D6EE76F3D70 foreign key (fk_parent_id) references tb_group
alter table tb_group_resource add constraint FK898FD3BF9CE20AF foreign key (fk_group_id) references tb_group
alter table tb_group_resource add constraint FK898FD3BFE0485F85 foreign key (fk_resource_id) references tb_resource
alter table tb_group_user add constraint FK92B07BFC9CE20AF foreign key (fk_group_id) references tb_group
alter table tb_group_user add constraint FK92B07BFCE614E7E5 foreign key (fk_user_id) references tb_user
alter table tb_resource add constraint FKECCF42BF7BAAAEE9 foreign key (fk_parent_id) references tb_resource