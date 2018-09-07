drop table if exists tb3;
create table tb3 (
    id bigint not null primary key,
    user_id varchar(100),
    traveldate DATE,
    fee decimal,
    days int
);
