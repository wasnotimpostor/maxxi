create table roles
(
    id        int auto_increment
        primary key,
    role_name varchar(255) null,
    constraint UK_nb4h0p6txrmfc0xbrd1kglp9t
        unique (role_name)
);

create table users
(
    id           char(36)     not null
        primary key,
    address      varchar(255) not null,
    code         varchar(255) not null,
    created_at   datetime(6)  not null,
    ktp_number   varchar(16)  not null,
    name         varchar(50)  not null,
    password     varchar(255) not null,
    phone_number varchar(255) not null,
    updated_at   datetime(6)  not null,
    username     varchar(50)  not null,
    constraint UK_71vrxovabe8x9tom8xwefi3e7
        unique (code),
    constraint UK_r43af9ap4edm43mmtq01oddj6
        unique (username)
);

create table user_roles
(
    user_id char(36) not null,
    role_id int      not null,
    primary key (user_id, role_id),
    constraint FKh8ciramu9cc9q3qcqiv4ue8a6
        foreign key (role_id) references roles (id),
    constraint FKhfh9dx7w3ubf1co1vdev94g3f
        foreign key (user_id) references users (id)
);