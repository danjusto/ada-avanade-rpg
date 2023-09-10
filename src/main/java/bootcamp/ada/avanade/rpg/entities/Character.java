package bootcamp.ada.avanade.rpg.entities;

import bootcamp.ada.avanade.rpg.dto.request.CharacterRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.CharacterResponseDTO;
import bootcamp.ada.avanade.rpg.models.CharClass;
import bootcamp.ada.avanade.rpg.models.CharacterClass;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "characters")
@Getter
@NoArgsConstructor
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CharClass characterClass;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Character(CharacterRequestDTO dto, User user) {
        this.name = dto.name();
        this.characterClass = dto.characterClass();
        this.user = user;
    }
    public CharacterResponseDTO dto() {
        return new CharacterResponseDTO(this.id, this.name, this.characterClass, this.user.getId());
    }
}
