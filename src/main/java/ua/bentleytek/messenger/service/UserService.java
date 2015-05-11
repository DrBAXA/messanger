package ua.bentleytek.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.UsersDAO;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.cash.OnlineUsersCash;

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
        if(user == null){
            user = usersDAO.findOne(id);
            initFriends(user);
        }
        return user;
    }

    public User getUser(String name){
        User user = onlineUsersCash.get(name);
        if(user == null){
            user = usersDAO.getByName(name);
            initFriends(user);
        }
        return user;
    }

    public void addUser(User user){
        user.setEnabled(true);
        user.setRole(DEFAULT_USER_ROLE);
        String rawPassword = user.getPassword();
        String codedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(codedPassword);
        usersDAO.save(user);
    }

    public List<User> getFriends(String name){
        return getUser(name).getFriends();
    }

    public Set<Integer> getOnlineFriends(String name){
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
            initFriends(user);
            user.setLastVisit();
            onlineUsersCash.add(user);
        }
    }


    /**
     * Load users friends and invitations from DB before adding to cash to prevent LazyInitializationException
     * instead of use FetchType.EAGER
     * because FetchType.EAGER loads friends recursively
     * i.e. loads friends of friends.
     * @param user
     */
    private void initFriends(User user){
        System.out.println(user.getFriends().size());
        System.out.println(user.getFriendInvitations().size());
    }

    public User find(String nameOrEmail) {
        return usersDAO.getByNameOrEmail(nameOrEmail);
    }

    /**
     * Add user given by invitorName to invitationSet of user given by userId
     * @param invitorName
     * @param userId
     * @return true if adde or false if already presented in this set
     */
    public boolean addInvitation(String invitorName, int userId) {
        User invitor = getUser(invitorName);
        User user = getUser(userId);
        boolean result = user.getFriendInvitations().add(invitor);
        usersDAO.save(user);
        return result;
    }

    /**
     * Move invitor from invitors to friends if it was in invitors
     * @param userName
     * @param invitorId
     * @return true if success or false if invitor wasn't found in invitors of this user
     */
    public boolean acceptInvitation(String userName, int invitorId){
        User user = getUser(userName);
        User invitor = getUser(invitorId);
        if(user.getFriendInvitations().contains(invitor)){
            user.getFriends().add(invitor);
            user.getFriendInvitations().remove(invitor);
            usersDAO.save(user);
            return true;
        }

        return false;
    }

    public boolean rejectInvitation(String name, int invitorId) {
        User user = getUser(name);
        User invitor = getUser(invitorId);
        boolean result = user.getFriendInvitations().remove(invitor);
        usersDAO.save(user);
        return result;
    }
}
