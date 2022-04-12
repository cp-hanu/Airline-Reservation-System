create table user
(
    id        bigint       not null auto_increment,
    email     varchar(255) not null,
    full_name varchar(255) not null,
    password  varchar(255) not null,
    primary key (id)
) ENGINE = InnoDB;

create table role
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) ENGINE = InnoDB;

create table user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id)
) ENGINE = InnoDB;
