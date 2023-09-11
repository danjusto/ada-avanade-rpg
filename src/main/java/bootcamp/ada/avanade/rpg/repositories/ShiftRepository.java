package bootcamp.ada.avanade.rpg.repositories;

import bootcamp.ada.avanade.rpg.entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
}
