package mir.repositories;

import mir.models.ParsedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IMessageRepository extends JpaRepository<ParsedMessage, Integer> {

//    // TODO: 3/1/2021 Need to write SQL queries with annotation @Query
//    /**
//     * Returns all messages with given MTI
//     * @param mti MTI
//     * @return list of messages
//     */
//    List<ParsedMessage> getByMti(String mti);
//
//    /**
//     * Returns a message with given transaction number
//     * @param transactionNumber Transaction number
//     * @return message
//     */
//    ParsedMessage getByTransactionNumber(String transactionNumber);
//
//    /**
//     * Returns list of messages in date range [start, end]
//     * @param start Start date
//     * @param end End date
//     * @return list of messages
//     */
//    List<ParsedMessage> getInDateRange(LocalDateTime start, LocalDateTime end);
//
//    /**
//     * Returns all messages with given HEX
//     * @param hex HEX
//     * @return list of messages
//     */
//    List<ParsedMessage> getByHex(String hex);
//
//    /**
//     * Returns all messages with given value of "edited"
//     * @param edited indicates if messages edited
//     * @return list of messages
//     */
//    List<ParsedMessage> getByEdited(boolean edited);
//
//    /**
//     * Deletes all messages with given MTI
//     * @param mti MTI
//     */
//    void deleteByMti(String mti);
//
//    /**
//     * Deletes one message with given transaction number
//     * @param transactionNumber Transaction number
//     */
//    void deleteByTransactionNumber(String transactionNumber);
//
//    /**
//     * Deletes all messages in given dateTime range
//     * @param start Start date
//     * @param end End date
//     */
//    void deleteInDateRange(LocalDateTime start, LocalDateTime end);
//
//    /**
//     * Deletes all messages with given HEX
//     * @param hex HEX
//     */
//    void deleteByHex(String hex);
//
//    /**
//     * Deletes all messages with given value of "edited"
//     * @param edited indicates if messages edited
//     */
//    void deleteByEdited(boolean edited);
}
