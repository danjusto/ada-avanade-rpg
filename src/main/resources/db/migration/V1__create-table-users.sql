CREATE TABLE IF NOT EXISTS users(
    id bigserial not null,
    name varchar(255) not null,
    email varchar(255) unique not null,
    password varchar(255) not null,
    primary key(id)
)