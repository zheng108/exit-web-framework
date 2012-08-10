
drop table tb_account_menu if exists
drop table tb_account_role if exists
drop table tb_account_role_menu if exists
drop table tb_account_user if exists
drop table tb_account_user_role if exists
create table tb_account_menu (id varchar(32) not null, name varchar(255), type integer not null, fk_parent_id varchar(32), primary key (id))
create table tb_account_role (id varchar(32) not null, name varchar(255), primary key (id))
create table tb_account_role_menu (role_id varchar(32) not null, menu_id varchar(32) not null)
create table tb_account_user (id varchar(32) not null, login_name varchar(255),create_time date, password varchar(255), pinyin_code varchar(255), real_name varchar(255), state integer, wubi_code varchar(255), primary key (id))
create table tb_account_user_role (user_id varchar(32) not null, role_id varchar(32) not null)
alter table tb_account_menu add constraint FKF1FD5662AF1EE0EC foreign key (fk_parent_id) references tb_account_menu
alter table tb_account_role_menu add constraint FKA099106578BE0711 foreign key (menu_id) references tb_account_menu
alter table tb_account_role_menu add constraint FKA0991065922C9BB1 foreign key (role_id) references tb_account_role
alter table tb_account_user_role add constraint FKA0D0DD47922C9BB1 foreign key (role_id) references tb_account_role
alter table tb_account_user_role add constraint FKA0D0DD4737575F91 foreign key (user_id) references tb_account_user
