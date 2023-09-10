package bootcamp.ada.avanade.rpg.entities;

import bootcamp.ada.avanade.rpg.dto.request.UserRequestDTO;
import bootcamp.ada.avanade.rpg.dto.response.UserResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    public User(UserRequestDTO dto) {
        this.name = dto.name();
        this.email = dto.email();
        this.password = dto.password();
    }
    public UserResponseDTO dto() {
        return new UserResponseDTO(this.id, this.name, this.email);
    }
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
    public void editNameAndEmail(String name, String email) {
        if (name != null) {
            this.name = name;
        }
        if (email != null) {
            this.email = email;
        }
    }
}
