create database jta1 default charset=utf8mb4 collate=utf8mb4_unicode_ci;
create database jta2 default charset=utf8mb4 collate=utf8mb4_unicode_ci;

grant all on jta1.* to dbuser@localhost identified by 'dbpass';
grant all on jta2.* to dbuser@localhost identified by 'dbpass';

flush privileges;
