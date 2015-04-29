package ua.bentleytek.messenger.dao;

import org.springframework.data.repository.CrudRepository;
import ua.bentleytek.messenger.entity.User;


public interface UsersDAO extends CrudRepository<User, Integer> {
    public User getByName(String name);

    public int countByName(String name);

    public int countByEmail(String email);
}
