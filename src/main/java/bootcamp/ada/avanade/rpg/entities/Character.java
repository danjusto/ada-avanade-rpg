package bootcamp.ada.avanade.rpg.entities;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterDetailsResponseDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterListDTO;
import bootcamp.ada.avanade.rpg.models.CharClass;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "characters")
@Getter
@Setter
@NoArgsConstructor
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CharClass characterClass;
    private int victories;
    private int defeats;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Character(CharacterRequestDTO dto, User user) {
        this.name = dto.name();
        this.characterClass = dto.characterClass();
        this.user = user;
        this.victories = 0;
        this.defeats = 0;
    }
    public CharacterDetailsResponseDTO dto() {
        return new CharacterDetailsResponseDTO(this.id, this.name, this.characterClass, this.victories, this.defeats, this.user.getId());
    }
    public CharacterListDTO dtoList() {
        return new CharacterListDTO(this.id, this.name, this.characterClass);
    }
    public void changeName(String newName) {
        this.name = newName;
    }
    public void updateVictoriesAndDefeats(Boolean victorie) {
        if(Boolean.TRUE.equals(victorie)) {
            this.victories++;
        } else {
            this.defeats++;
        }
    }
}
