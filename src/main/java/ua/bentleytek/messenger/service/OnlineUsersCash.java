package ua.bentleytek.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.UsersDAO;
import ua.bentleytek.messenger.entity.User;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUsersCash {
    @Autowired
    UsersDAO usersDAO;

    private Thread cleaner;

    private Map<Integer, User> onLineById = new ConcurrentHashMap<>();
    private Map<String, Integer> onLineByName = new ConcurrentHashMap<>();

    public void add(User user){
        onLineById.put(user.getId(), user);
        onLineByName.put(user.getName(), user.getId());
    }

    public User get(int id){
        return onLineById.get(id);
    }

    public User get(String name){
        return onLineById.get(onLineByName.get(name));
    }

    public boolean contains(int id){
        return onLineById.containsKey(id);
    }

    public boolean contains(String name){
        return onLineByName.containsKey(name);
    }

    public Set<Integer> getOnline(){
        Set<Integer> result = new HashSet<>();
        for(User user : onLineById.values()){
            if(user.isOnline()){
                result.add(user.getId());
            }
        }
        return result;
    }

    private void refresh(){
        for(Integer id : onLineById.keySet()){
            User user = onLineById.get(id);
            if(! user.isOnline()){
                onLineByName.remove(user.getName());
                onLineById.remove(id);
                usersDAO.save(user);
            }
        }
    }

    @PostConstruct
    private void runCleaner(){
        cleaner = new Cleaner();
        cleaner.run();
    }

    private class Cleaner extends Thread{
        @Override
        public void run(){
            while (! isInterrupted()){
                refresh();
                try {
                    sleep(60000);
                } catch (InterruptedException ie) {
                    //do nothing
                }
            }
        }
    }
}
