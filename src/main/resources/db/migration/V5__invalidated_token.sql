create table invalidated_token (
    id varchar(36) not null primary key,
    expiry_time timestamp not null
);

create index idx_invalidated_token_expiry_time
    on invalidated_token (expiry_time);
