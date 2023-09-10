package bootcamp.ada.avanade.rpg.repositories;

import bootcamp.ada.avanade.rpg.entities.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByUserIdAndName(Long userId, String name);
}
