package bootcamp.ada.avanade.rpg.repositories;

import bootcamp.ada.avanade.rpg.entities.Battle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BattleRepository extends JpaRepository<Battle, Long> {
    Optional<Battle> findByIdAndCharacterId(Long id, Long characterId);
}
