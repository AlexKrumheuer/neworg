create table revoked_tokens(
    id bigint primary key auto_increment,
    token varchar(500) unique
);