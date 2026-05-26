alter table user_roles
    change column role role_name varchar(50) not null;

insert into roles (name, description)
select distinct
    ur.role_name,
    case ur.role_name
        when 'ADMIN' then 'System administrator'
        when 'USER' then 'Default application user'
        else concat('Role ', ur.role_name)
    end
from user_roles ur
left join roles r on r.name = ur.role_name
where r.name is null;

alter table user_roles
    add constraint fk_user_roles_role
        foreign key (role_name) references roles(name)
        on delete cascade;
