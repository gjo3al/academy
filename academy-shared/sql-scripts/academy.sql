drop database IF EXISTS academy;
create database IF not EXISTS academy
CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

use academy;

SET FOREIGN_KEY_CHECKS = 0;

create table users(
	id int not null primary key auto_increment,
	username varchar(68) not null unique,
	password varchar(68) not null,
	enabled boolean not null,
	detail_id int not null,
    index (username),
	constraint fk_users_user_detail foreign key(detail_id) references user_detail(id)
);

create table authorities (
	users_id int not null,
	authority varchar(50) not null,
    primary key (users_id, authority),
	constraint fk_authorities_users foreign key(users_id) references users(id)
);

create table user_detail(
	id int not null auto_increment primary key,
	nickname varchar(68) not null,
	email varchar(68) not null unique
);

create table audit(
	id int not null auto_increment primary key,
	users_id int not null,
    address varchar(20) not null,
	timeatamp datetime not null,
    index (users_id),
    constraint fk_audit_users foreign key(users_id) references users(id),
    constraint uq_users_id_timeatamp unique(users_id, timeatamp)
);

SET FOREIGN_KEY_CHECKS = 1;