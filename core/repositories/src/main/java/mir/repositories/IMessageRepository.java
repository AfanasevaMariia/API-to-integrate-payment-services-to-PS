package mir.repositories;

import mir.models.ParsedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMessageRepository extends JpaRepository<ParsedMessage, Integer> {

}
