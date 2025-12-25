create table transactions(
    id bigint primary key auto_increment,
    asset bigint not null,
    quantity decimal(19,4) not null,
    price decimal(19,4) not null,
    transaction_date datetime not null,
    type varchar(4) not null,

    foreign key (asset) references asset(id)
    on delete cascade
);