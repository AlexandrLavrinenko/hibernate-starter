-- For complex id, which consist a few fields:
drop table users;
create table users
(
    --- KEYS ----------------------
    firstname  VARCHAR(128),
    lastname   VARCHAR(128),
    birth_date DATE,
    -------------------------------
    username   VARCHAR(128) UNIQUE,
    role       VARCHAR(12),
    info       JSONB,
    PRIMARY KEY (firstname, lastname, birth_date)   -- All columns is NOT NULL (auto)
);
-- --------------------------------------------------------------------------------------------
-- For @GeneratedValue(generator = "custom_user_generator", strategy = GenerationType.SEQUENCE)
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