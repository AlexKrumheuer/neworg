create table asset(
    id bigint primary key auto_increment,
    ticker varchar(255) not null unique,
    name varchar(255) not null,
    type varchar(50) not null
);

