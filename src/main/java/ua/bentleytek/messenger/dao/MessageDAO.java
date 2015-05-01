package ua.bentleytek.messenger.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;

import java.sql.Timestamp;

/**
 *
 */
public interface MessageDAO extends CrudRepository<Message, Integer> {
    @Query(value = "SELECT * FROM messages WHERE (user_from = ?1 AND user_to = ?2) OR (user_from = ?2 AND user_to = ?1)" +
            " ORDER BY date DESC LIMIT ?3, ?4", nativeQuery = true)
    Iterable<Message> getByUser(User user, User friend, int first, int count);

    @Query(value = "SELECT * FROM messages WHERE  user_to = ?1 AND is_read = 0", nativeQuery = true)
    Iterable<Message> getUnread(User user);

}
