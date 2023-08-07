DROP TABLE payment;
CREATE TABLE payment
(
    id BIGSERIAL PRIMARY KEY,
    amount INT NOT NULL,
    version BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL REFERENCES users
);
-- --------------------------------------------------------------------------------------------
CREATE TABLE company_locale
(
    company_id  INT          NOT NULL REFERENCES company (id),
    lang        CHAR(2)      NOT NULL,
    description VARCHAR(128) NOT NULL,
    PRIMARY KEY (company_id, lang)
);

-- --------------------------------------------------------------------------------------------
DROP TABLE users_chat;
CREATE TABLE users_chat
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users (id) NOT NULL UNIQUE,
    chat_id    BIGINT REFERENCES chat (id)  NOT NULL UNIQUE,
    created_at TIMESTAMP                    NOT NULL,
    created_by VARCHAR(64)                  NOT NULL,
    UNIQUE (user_id, chat_id)
);

DROP TABLE chat;
CREATE TABLE chat
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

-- --------------------------------------------------------------------------------------------
-- For complex id, which consist a few fields:
drop table users;
create table users
(
    id         BIGSERIAL PRIMARY KEY,      -- PRIMARY KEY = NOT NULL + UNIQUE
    --- KEYS ----------------------
    firstname  VARCHAR(128),
    lastname   VARCHAR(128),
    birth_date DATE,
    -------------------------------
    username   VARCHAR(128) UNIQUE,
    role       VARCHAR(12),
    info       JSONB,
    -------------------------------
    company_id INT REFERENCES company (id) -- ON DELETE cascade (DB will delete ALL users if their company will be delete)
    -------------------------------
    --PRIMARY KEY (firstname, lastname, birth_date)   -- All columns is NOT NULL (auto)
);

DROP TABLE profile;
-- The best option is with a synthetic auto-generated key
CREATE TABLE profile
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT NOT NULL UNIQUE REFERENCES users (id),
    street   VARCHAR(128),
    language CHAR(2)
);
-- --------------------------------------------------------------------------------------------
-- Option when the primary key (PRIMARY KEY) is also a FOREIGN KEY too
CREATE TABLE profile
(
    user_id  BIGINT PRIMARY KEY REFERENCES users (id),
    street   VARCHAR(128),
    language CHAR(2)
);
-- --------------------------------------------------------------------------------------------
CREATE TABLE company
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);
-- --------------------------------------------------------------------------------------------
--  Without generated key
DROP TABLE users_chat;
CREATE TABLE users_chat
(
    user_id BIGINT REFERENCES users (id) NOT NULL UNIQUE,
    chat_id BIGINT REFERENCES chat (id)  NOT NULL UNIQUE,
    PRIMARY KEY (user_id, chat_id)
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