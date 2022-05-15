create sequence hibernate_sequence;

create table customer (
    id bigint not null primary key,
    name varchar(128),
    surname varchar(128),
    balance bigint
);

create table account (
    id bigint not null primary key,
    customer_id bigint not null references customer (id)
);

create table transactions (
    id bigint not null primary key,
    account_id bigint not null references account (id),
    transactions bigint
);
