package ua.bentleytek.messenger.service.cash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.MessageDAO;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.util.ByDateMessageComparator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class OnlineMessagesCash extends Cleanable {

    private OnlineUsersCash usersCash;
    private MessageDAO messageDAO;

    private Map<Integer, Set<Message>> newMessages = new ConcurrentHashMap<>();
    private Map<Integer, AtomicBoolean> changes = new ConcurrentHashMap<>();

    @Autowired
    public OnlineMessagesCash(OnlineUsersCash usersCash, MessageDAO messageDAO) {
        this.usersCash = usersCash;
        this.messageDAO = messageDAO;
    }

    /**
     * Add user in cash
     * @param id
     */
    public void register(int id){
        if(! newMessages.containsKey(id)){
            newMessages.put(id, new TreeSet<>(new ByDateMessageComparator()));
            changes.put(id, new AtomicBoolean(false));
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
            changes.get(userId).set(true);
            return true;
        }
        return false;
    }

    /**
     * @return  last count messages from conversation between user and friend given by their id
     */
    public Set<Message> get(int userId, int friendId, int count){

        Set<Message> result = new TreeSet<>(new ByDateMessageComparator());
        Set<Message> buffer = new TreeSet<>(new ByDateMessageComparator());

        if(newMessages.containsKey(userId)) {
            //Put to result messages sent from friend to this user
            for (Message message : newMessages.get(userId)) {
                if (message.getFrom().getId() == friendId) {
                    buffer.add(message);
                }
            }
        }
        if(newMessages.containsKey(friendId)){
            //Put to result messages sent  from this user to friend
            for(Message message : newMessages.get(friendId)){
                if(message.getFrom().getId() == userId){
                    buffer.add(message);
                }
            }
        }
        Iterator<Message> messageIterator = buffer.iterator();
        while (messageIterator.hasNext() && count-- >0){
            result.add(messageIterator.next());
        }
        return result;
    }

    /**
     * Returns all messages that was sent to this user
     * @param userId
     * @return
     */
    public Set<Message> get(int userId){
        if(newMessages.containsKey(userId)){
            return newMessages.get(userId);
        }
        return Collections.EMPTY_SET;
    }

    /**
     * @param userId
     * @return true if since last check new messages received and false otherwise.
     */
    public boolean hasNew(int userId){
        return changes.containsKey(userId) ?  changes.get(userId).getAndSet(false) : false;
    }

    public void resetNew(int userId){
        if(changes.containsKey(userId)) {
            changes.get(userId).set(false);
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
                changes.remove(id);
            }
        }
    }

    /**
     * Save to DB, set as read and delete from cash messages to userId from friendId
     * @param userId
     * @param friendId
     */
    public void markRead(int userId, int friendId){
        if(newMessages.containsKey(userId)){
            Iterator<Message> iterator = newMessages.get(userId).iterator();
            while (iterator.hasNext()){
                Message message = iterator.next();
                if(message.getFrom().getId() == friendId){
                    message.setRead();
                    messageDAO.save(message);
                    iterator.remove();
                }
            }

        }
    }
}
