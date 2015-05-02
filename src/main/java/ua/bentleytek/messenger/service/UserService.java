package ua.bentleytek.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.UsersDAO;
import ua.bentleytek.messenger.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    OnlineUsersCash onlineUsersCash;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User getUser(int id){
        User user = onlineUsersCash.get(id);
        return user != null ? user : usersDAO.findOne(id);
    }

    public User getUser(String name){
        User user = onlineUsersCash.get(name);
        return user != null? user : usersDAO.getByName(name);
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

    public Set<Integer> getOnline(String name){
        Set<Integer> result = new HashSet<>();
        for(User user : getUser(name).getFriends()){
            int id = user.getId();
            if(onlineUsersCash.contains(id)){
                result.add(id);
            }
        }
        return result;
    }

    public void setLastVisit(String name){
        User user = onlineUsersCash.get(name);
        if(user != null){
            user.setLastVisit();
        }else{
            user = usersDAO.getByName(name);
            user.setLastVisit();
            onlineUsersCash.add(user);
        }
    }
}
