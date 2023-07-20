drop table users;
create table users
(
    id         BIGINT primary key, -- if USERS_ID_SEQ exists, then BIGSERIAL not use, use BIGINT
    username   varchar(128) unique,
    firstname  varchar(128),
    lastname   varchar(128),
    birth_date date,
    role       varchar(12),
    info       jsonb
);

create sequence users_id_seq
owned by users.id;