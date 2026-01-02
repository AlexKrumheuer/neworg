create table users(
    id bigint primary key auto_increment,
    username varchar(50) not null unique,
    password varchar(255) not null,
    email varchar(255) not null unique,
    created_at timestamp default current_timestamp
);