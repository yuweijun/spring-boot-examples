drop database if exists dbtest1;
create database dbtest1;

drop database if exists dbtest2;
create database dbtest2;

drop database if exists dbtest3;
create database dbtest3;

drop database if exists dbtest4;
create database dbtest4;

drop database if exists dbtest5;
create database dbtest5;

grant all on dbtest1.* to dbuser@'%' identified by 'dbpass';
grant all on dbtest2.* to dbuser@'%' identified by 'dbpass';
grant all on dbtest3.* to dbuser@'%' identified by 'dbpass';
grant all on dbtest4.* to dbuser@'%' identified by 'dbpass';
grant all on dbtest5.* to dbuser@'%' identified by 'dbpass';
flush privileges;