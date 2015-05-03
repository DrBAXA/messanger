package ua.bentleytek.messenger.service.cash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.MessageDAO;
import ua.bentleytek.messenger.entity.Message;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineMessagesCash extends Cleanable {

    @Autowired
    OnlineUsersCash usersCash;
    @Autowired
    MessageDAO messageDAO;

    private Map<Integer, Set<Message>> newMessages = new ConcurrentHashMap<>();

    /**
     * Add user in cash
     * @param id
     */
    public void register(int id){
        if(! newMessages.containsKey(id)){
            newMessages.put(id, new HashSet<Message>());
        }
    }

    /**
     * Check if user is registered in cash
     * @param id
     * @return
     */
    public boolean registered(int id){
        return newMessages.containsKey(id);
    }
    /**
     * Add message to cash if user is registered in this cash
     * @see #register(int)
     * @param message
     * @return true if message was added
     */
    public boolean put(Message message){
        int userId = message.getTo().getId();
        if(newMessages.containsKey(userId)){
            newMessages.get(userId).add(message);
            return true;
        }
        return false;
    }

    /**
     * @return  set of new messages for given userId received from given friend
     * or all friends if friendId <= 0
     */
    public Set<Message> get(int userId, int friendId){
        if(newMessages.containsKey(userId)){
            Set<Message> result = new HashSet<>();
            result.addAll(newMessages.get(userId));
            Iterator<Message> iterator = result.iterator();
            while (iterator.hasNext()){
                Message message = iterator.next();
                if(message.getFrom().getId() != friendId && friendId > 0){
                    iterator.remove();
                }else if(message.getFrom().getId() == friendId){
                    newMessages.get(userId).remove(message);
                }
            }
            return result;
        }else {
            return null;
        }
    }

    /**
     * @param id
     * @return count of new messages for given user
     *         -1 if that user is not in
     */
    public int getCount(int id){
        if(newMessages.containsKey(id)){
            return newMessages.get(id).size();
        }else {
            return -1;
        }
    }

    /**
     * Save to DB and delete from cash messages to users that there isn't online
     */
    public void clean(){
        for(int id : newMessages.keySet()){
            if(! usersCash.contains(id)){
                messageDAO.save(newMessages.get(id));
            }
        }
        Iterator<Map.Entry<Integer, Set<Message>>> iterator = newMessages.entrySet().iterator();
        while (iterator.hasNext()){
            int id = iterator.next().getKey();
            if(! usersCash.contains(id)){
                messageDAO.save(newMessages.get(id));
                iterator.remove();
            }
        }
    }
}
