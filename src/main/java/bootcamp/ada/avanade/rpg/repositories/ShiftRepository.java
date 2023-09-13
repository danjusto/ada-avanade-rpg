package bootcamp.ada.avanade.rpg.repositories;

import bootcamp.ada.avanade.rpg.entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByBattleIdAndActiveTrue(Long battleId);
    Optional<Shift> findFirstByBattleIdAndActiveFalseOrderByIdDesc(Long battleId);
    Optional<Shift> findByIdAndActiveTrue(Long id);
}
