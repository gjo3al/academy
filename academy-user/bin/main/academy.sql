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
    index (username)
);

create table authorities (
	users_id int not null,
	authority varchar(50) not null,
    primary key (users_id, authority),
	constraint fk_authorities_users foreign key(users_id) references users(id)
);

create table user_detail(
	users_id int not null,
	nickname varchar(68) not null,
	email varchar(68) not null unique,
    primary key (users_id),
    constraint fk_detail_users foreign key(users_id) references users(id)
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

create table course(
	id int not null auto_increment primary key,
    name varchar(20) not null,
	description varchar(200),
    instructor_id int not null,
    index (instructor_id),
    constraint fk_course_users foreign key(instructor_id) references users(id)
);

create table course_student(
	student_id int,
    course_id int,
    primary key(student_id, course_id),
    constraint fk_student_course foreign key(student_id) references users(id),
    constraint fk_course_student foreign key(course_id) references course(id)
);

SET FOREIGN_KEY_CHECKS = 1;