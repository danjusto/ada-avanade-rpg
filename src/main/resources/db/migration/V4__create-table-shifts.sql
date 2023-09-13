CREATE TABLE IF NOT EXISTS shifts(
    id bigserial not null,
    round int not null,
    dice_atk_character int,
    dice_def_character int,
    dice_atk_monster int,
    dice_def_monster int,
    character_hit boolean,
    monster_hit boolean,
    damage_character int,
    damage_monster int,
    pv_character int,
    pv_monster int,
    active boolean not null,
    battle_id bigint not null,
    primary key(id),
    constraint fk_shifts_battle_id foreign key (battle_id) references battles(id)
)