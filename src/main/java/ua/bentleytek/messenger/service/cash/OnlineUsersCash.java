package ua.bentleytek.messenger.service.cash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.UsersDAO;
import ua.bentleytek.messenger.entity.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUsersCash extends Cleanable {

    @Autowired
    Environment env;

    private Map<Integer, User> onLineById = new ConcurrentHashMap<>();
    private Map<String, Integer> onLineByName = new ConcurrentHashMap<>();

    private UsersDAO usersDAO;
    @Autowired
    public OnlineUsersCash(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public void add(User user){
        onLineById.put(user.getId(), user);
        onLineByName.put(user.getName(), user.getId());
    }

    public User get(int id){
        return onLineById.get(id);
    }

    public User get(String name){
        Integer id = onLineByName.get(name);
        if(id != null) {
            return onLineById.get(id);
        }else {
            return null;
        }
    }

    public boolean contains(int id){
        return onLineById.containsKey(id);
    }

    public void clean(){
        for(Integer id : onLineById.keySet()){
            User user = onLineById.get(id);
            if(System.currentTimeMillis() - user.getLastVisit().getTime() > env.getProperty("timeout.online", Integer.class)){
                onLineByName.remove(user.getName());
                onLineById.remove(id);
                usersDAO.save(user);
            }
        }
    }

}
