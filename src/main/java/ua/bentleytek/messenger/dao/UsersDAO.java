package ua.bentleytek.messenger.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ua.bentleytek.messenger.entity.User;


public interface UsersDAO extends CrudRepository<User, Integer> {
    public User getByName(String name);

    @Query(value = "SELECT * FROM users WHERE  name = ?1 OR email = ?1", nativeQuery = true)
    public User getByNameOrEmail(String nameOrEmail);

    public int countByName(String name);

    public int countByEmail(String email);
}
