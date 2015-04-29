package ua.bentleytek.messenger.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;

/**
 *
 */
public interface MessageDAO extends CrudRepository<Message, Integer> {
    @Query(value = "SELECT * FROM messages WHERE (user_from = ?1 AND user_to = ?2) OR (user_from = ?2 AND user_to = ?1) LIMIT ?3, ?4", nativeQuery = true)
    Iterable<Message> getByUser(User user, User friend, int first, int count);
}
