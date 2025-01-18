create table users
(
    user_id  bigserial
        primary key,
    username varchar(30) not null unique,
    password varchar(80) not null,
    email    varchar(50)
        unique
);

create table roles
(
    role_id serial
        primary key,
    name    varchar(50) not null
);

create table users_roles
(
    user_id bigint  not null
        references users,
    role_id integer not null
references roles,
    primary key (user_id, role_id)
);