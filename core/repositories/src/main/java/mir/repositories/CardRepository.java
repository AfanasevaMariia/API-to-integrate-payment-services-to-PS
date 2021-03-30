package mir.repositories;

import mir.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {

    boolean existsByNumber(String number);

    Card findByNumber(String number);
}
