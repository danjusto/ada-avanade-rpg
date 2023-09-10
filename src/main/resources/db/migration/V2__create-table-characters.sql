CREATE TABLE IF NOT EXISTS characters(
    id bigserial not null,
    name varchar(255) not null,
    character_class varchar(255) not null,
    victories int not null,
    defeats int not null,
    user_id bigint not null,
    primary key(id),
    constraint fk_characters_user_id foreign key (user_id) references users(id)
)