-- Write your Task 1 answers in this file  
drop database if exists bedandbreakfast;
create database bedandbreakfast;

use bedandbreakfast;

create table users (
   email varchar(128),
   name varchar(128)
);

create table bookings (
   booking_id char(8) not null,
   listing_id varchar(20),
   duration int,
   email varchar(128) not null,

   constraint pk_listing_id primary key (listing_id)
);

create table reviews (
   id int auto_increment,
   date timestamp default current_timestamp on update current_timestamp,
   listing_id varchar(20) not null,
   reviewer_name varchar(64),
   comments text,
	
   constraint pk_id primary key (id)
);

grant all privileges on bedandbreakfast.* to fred@'%';

flush privileges;
