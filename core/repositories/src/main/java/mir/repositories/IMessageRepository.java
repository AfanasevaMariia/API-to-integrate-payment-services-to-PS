package mir.repositories;

import mir.models.ParsedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IMessageRepository extends JpaRepository<ParsedMessage, Integer> {
    // TODO: 2/3/2021 Need to test SQL queries
    /**
     * Returns all messages with given MTI
     * @param mti MTI
     * @return list of messages
     */
    @Query("SELECT * FROM public.parsed_message WHERE mti = :mti")
    List<ParsedMessage> getByMti(@Param("mti") String mti);

    /**
     * Returns a message with given transaction number
     * @param transactionNumber Transaction number
     * @return message
     */
    @Query("SELECT * FROM public.parsed_message WHERE transaction_number = :transactionNumber")
    ParsedMessage getByTransactionNumber(@Param("transactionNumber") String transactionNumber);

    /**
     * Returns list of messages in date range [start, end]
     * @param start Start date
     * @param end End date
     * @return list of messages
     */
    @Query( "SELECT * FROM public.parsed_message " +
            "WHERE transaction_date >= CAST(':start' AS TIMESTAMP)" +
            "AND transaction_date <= CAST(':end' AS TIMESTAMP)")
    List<ParsedMessage> getInDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    /**
     * Returns all messages with given HEX
     * @param hex HEX
     * @return list of messages
     */
    @Query("SELECT * FROM public.parsed_message WHERE hex = :hex")
    List<ParsedMessage> getByHex(@Param("hex") String hex);

    /**
     * Returns all messages with given value of "edited"
     * @param edited indicates if messages edited
     * @return list of messages
     */
    @Query("SELECT * FROM public.parsed_message WHERE edited = :edited")
    List<ParsedMessage> getByEdited(@Param("edited") boolean edited);

    /**
     * Deletes all messages with given MTI
     * @param mti MTI
     */
    @Query("DELETE FROM public.parsed_message WHERE mti = :mti")
    void deleteByMti(@Param("mti") String mti);

    /**
     * Deletes one message with given transaction number
     * @param transactionNumber Transaction number
     */
    @Query("DELETE FROM public.parsed_message WHERE transaction_number = :transactionNumber")
    void deleteByTransactionNumber(@Param("transactionNumber") String transactionNumber);

    /**
     * Deletes all messages in given dateTime range
     * @param start Start date
     * @param end End date
     */
    @Query( "DELETE FROM public.parsed_message " +
            "WHERE transaction_date >= CAST(':start' AS TIMESTAMP)" +
            "AND transaction_date <= CAST(':end' AS TIMESTAMP)")
    void deleteInDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    /**
     * Deletes all messages with given HEX
     * @param hex HEX
     */
    @Query("DELETE FROM public.parsed_message WHERE hex = :hex")
    void deleteByHex(@Param("hex") String hex);

    /**
     * Deletes all messages with given value of "edited"
     * @param edited indicates if messages edited
     */
    @Query("SELECT * FROM public.parsed_message WHERE edited = :edited")
    void deleteByEdited(@Param("edited") boolean edited);
}
