package bootcamp.ada.avanade.rpg.entities;

import bootcamp.ada.avanade.rpg.dto.response.AttackDTO;
import bootcamp.ada.avanade.rpg.dto.response.DamageResponseDTO;
import bootcamp.ada.avanade.rpg.dto.response.DefenseDTO;
import bootcamp.ada.avanade.rpg.dto.response.ShiftDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    private int diceDefMonster;
    private Boolean characterHit;
    private int diceAtkMonster;
    private int diceDefCharacter;
    private Boolean monsterHit;
    private int damageCharacter;
    private int damageMonster;
    private int pvCharacter;
    private int pvMonster;
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "battle_id")
    private Battle battle;
    public void initialize(Battle battle, int pvCharacter, int pvMonster) {
        this.round = battle.getNumberShifts();
        this.battle = battle;
        this.pvCharacter = pvCharacter;
        this.pvMonster = pvMonster;
        this.active = true;
    }
    public void updateAtk(int diceAtk, int diceDef, Boolean hit) {
        this.diceAtkCharacter = diceAtk;
        this.diceDefMonster = diceDef;
        this.characterHit = hit;
    }
    public void updateDef(int diceAtk, int diceDef, Boolean hit) {
        this.diceAtkMonster = diceAtk;
        this.diceDefCharacter = diceDef;
        this.monsterHit = hit;
    }
    public void updateCharacterDmgAndMonsterHP(int diceDamage) {
        this.damageCharacter = diceDamage;
        this.pvMonster = Math.max(this.pvMonster - diceDamage, 0);
    }
    public void updateMonsterDmgAndCharacterHP(int diceDamage) {
        this.damageMonster = diceDamage;
        this.pvCharacter = Math.max(this.pvCharacter - diceDamage, 0);
    }
    public ShiftDTO dto() {
        return new ShiftDTO(
                this.round,
                this.diceAtkCharacter,
                this.diceDefMonster,
                this.diceAtkMonster,
                this.diceDefCharacter,
                this.characterHit,
                this.monsterHit,
                this.damageCharacter,
                this.damageMonster,
                this.pvCharacter,
                this.pvMonster
        );
    }
    public AttackDTO attackCharacterDTO() {
        return new AttackDTO(this.id, this.characterHit, this.diceAtkCharacter, this.diceDefMonster);
    }
    public DefenseDTO defenseCharacterDTO() {
        return new DefenseDTO(this.id, this.monsterHit, this.diceDefCharacter, this.diceAtkMonster);
    }
    public DamageResponseDTO damageCharacterDTO() {
        return new DamageResponseDTO(this.id, this.damageCharacter, this.pvMonster);
    }
    public DamageResponseDTO damageMonsterDTO() {
        return new DamageResponseDTO(this.id, this.damageMonster, this.pvCharacter);
    }
}
