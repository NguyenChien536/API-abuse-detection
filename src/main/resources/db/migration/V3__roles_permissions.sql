create table roles (
    name varchar(50) not null primary key,
    description varchar(100) not null
);

create table permissions (
    name varchar(50) not null primary key,
    description varchar(100) not null
);

create table role_permissions (
    role_name varchar(50) not null,
    permission_name varchar(50) not null,
    primary key (role_name, permission_name),
    constraint fk_role_permissions_role
        foreign key (role_name) references roles(name)
        on delete cascade,
    constraint fk_role_permissions_permission
        foreign key (permission_name) references permissions(name)
        on delete cascade
);
