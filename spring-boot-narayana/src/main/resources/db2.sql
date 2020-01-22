drop table employees if exists;
create table employees(id serial,first_name varchar(255),last_name varchar(255));
insert into employees(first_name, last_name) values('c','d');
insert into employees(first_name, last_name) values('a','b');