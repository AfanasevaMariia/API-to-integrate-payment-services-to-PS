package mir.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import mir.models.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Integer> {
}
