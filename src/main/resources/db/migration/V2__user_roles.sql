create table user_roles (
    user_id bigint not null,
    role varchar(20) not null,
    primary key (user_id, role),
    constraint fk_user_roles_user
        foreign key (user_id) references users(id)
        on delete cascade
);

insert into user_roles (user_id, role)
select id, role
from users;

alter table users drop column role;
