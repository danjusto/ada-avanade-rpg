package bootcamp.ada.avanade.rpg.entities;

import bootcamp.ada.avanade.rpg.dto.response.AttackDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@NoArgsConstructor
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int round;
    private int diceAtkCharacter;
    private int avgAtkCharacter;
    private int diceDefMonster;
    private int avgDefMonster;
    private boolean characterHit;
    private int diceAtkMonster;
    private int avgAtkMonster;
    private int diceDefCharacter;
    private int avgDefCharacter;
    private boolean monsterHit;
    private int damageCharacter;
    private int damageMonster;
    private int pvCharacter;
    private int pvMonster;
    @ManyToOne
    @JoinColumn(name = "battle_id")
    private Battle battle;
    public Shift(Battle battle) {
        this.round = battle.getNumberShifts();
        this.battle = battle;
    }
    public AttackDTO attackCharacterDTO() {
        return new AttackDTO(this.characterHit, this.diceAtkCharacter, this.diceDefMonster, this.avgAtkCharacter, this.avgDefMonster);
    }
}
