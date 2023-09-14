package bootcamp.ada.avanade.rpg.entities;

import bootcamp.ada.avanade.rpg.dto.response.BattleDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterDetailsResponseDTO;
import bootcamp.ada.avanade.rpg.models.characters.CharacterClass;
import bootcamp.ada.avanade.rpg.models.Initiative;
import bootcamp.ada.avanade.rpg.models.MonsterClass;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "battles")
@NoArgsConstructor
@Getter
@Setter
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private MonsterClass monster;
    @Enumerated(EnumType.STRING)
    private Initiative initiative;
    private LocalDateTime date;
    @OneToMany(mappedBy = "battle")
    private List<Shift> shifts;
    private Boolean active;
    private int numberShifts;
    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character character;
    public Battle(MonsterClass monster, Initiative initiative, Character character) {
        this.monster = monster;
        this.initiative = initiative;
        this.character = character;
        this.active = true;
        this.numberShifts = 1;
        this.date = LocalDateTime.now();
    }
    public BattleDTO dto(CharacterDetailsResponseDTO characterDto) {
        return new BattleDTO(this.id, this.monster, this.initiative, characterDto);
    }
    public void endBattle() {
        this.active = false;
    }
    public void nextShift() {
        this.numberShifts++;
    }
    public CharacterClass getCharacterClass() {
        return this.character.getCharacterClass().getInstanceOfCharacterClass();
    }
    public CharacterClass getMonsterClass() {
        return this.monster.getEnumCharClass().getInstanceOfCharacterClass();
    }
}
