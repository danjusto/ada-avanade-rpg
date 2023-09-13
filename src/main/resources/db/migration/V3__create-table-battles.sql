CREATE TABLE IF NOT EXISTS battles(
    id bigserial not null,
    monster varchar(255) not null,
    initiative varchar(255) not null,
    date timestamp not null,
    active boolean not null,
    number_shifts int not null,
    character_id bigint not null,
    primary key(id),
    constraint fk_battles_character_id foreign key (character_id) references characters(id)
)