package ua.bentleytek.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.UsersDAO;
import ua.bentleytek.messenger.entity.User;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    @Autowired
    private UsersDAO usersDAO;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User getUser(int id){
        return usersDAO.findOne(id);
    }

    public User getUser(String name){
        return usersDAO.getByName(name);
    }

    public void addUser(User user){
        user.setRole(DEFAULT_USER_ROLE);
        String rawPassword = user.getPassword();
        String codedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(codedPassword);
        usersDAO.save(user);
    }

    public List<User> getFriends(String name){
        return getUser(name).getFriends();
    }

    public Map<Integer, Boolean> getOnline(String name){
        Map<Integer, Boolean> result = new HashMap<>();
        User user = getUser(name);
        for(User friend : user.getFriends()){
            if (friend.isOnline()){
                result.put(friend.getId(), true);
            }
        }
        return result;
    }

    public void setLastVisit(String name){
        User user = getUser(name);
        user.setLastVisit(new Timestamp(System.currentTimeMillis()));
        usersDAO.save(user);
    }
}
