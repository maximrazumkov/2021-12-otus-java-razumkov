create table address
(
    id bigserial not null primary key,
    street varchar(1024) not null
);
create table client
(
    id bigserial not null primary key,
    name varchar(256) not null,
    address_id bigint not null references address(id)
);
create table phone
(
    id bigserial not null primary key,
    number varchar(32) not null,
    client_id bigint not null references client(id)
);
create table usr
(
    id bigserial not null primary key,
    name varchar(256) not null,
    login varchar(32) not null,
    password varchar(32) not null
);