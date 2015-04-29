package ua.bentleytek.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.UsersDAO;
import ua.bentleytek.messenger.entity.User;

import java.util.Set;

@Service
public class UserService {

    public static final String DEFAULT_USER_ROLE = "ROLE_USER";

    @Autowired
    UsersDAO usersDAO;


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    public Set<User> getFriends(String name){
        return getUser(name).getFriends();
    }
}
