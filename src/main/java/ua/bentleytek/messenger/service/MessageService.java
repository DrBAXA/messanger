package ua.bentleytek.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.MessageDAO;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.cash.OnlineMessagesCash;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    OnlineMessagesCash messagesCash;
    @Autowired
    LongQueryEventService eventHandler;

    /**
     *
     * @return count unread messages for each user that sent still unread message
     */
    public Map<Integer, Integer> getUnreadCountBySender(User user){
        Iterable<Message> messages;

        if(messagesCash.registered(user.getId())){
            messages = messagesCash.get(user.getId());
        }else {
            messagesCash.register(user.getId());
            messages = messageDAO.getUnread(user);
            for(Message message:messages) {
                messagesCash.put(message);
            }
        }

        Map<Integer, Integer> result = new HashMap<>();
        for (Message message : messages){
            int userID = message.getFrom().getId();
            if(result.containsKey(userID)){
                result.put(userID, result.get(userID) + 1);
            }else {
                result.put(userID, 1);
            }
        }
        return result;
    }

    /**
     * Get messages that is sent to given user from given friend
     * Get its both from cash and DB
     * if first is more than 0(e.i. loads old messages) cash will not be used
     */
    public Iterable<Message> getMessages(User user, User friend, int first, int count){
        ArrayList<Message> result = new ArrayList<>();
        //Load messages from cash and add to result
        if(messagesCash.registered(user.getId()) && first == 0) {
            for(Message message : messagesCash.get(user.getId(), friend.getId(), count)){
                result.add(message);
            }
        }
        //Load messages from DB, add to result and set as read if required
        int remains = count - result.size();
        if(remains > 0){
            for(Message message : messageDAO.getByUser(user, friend, first+result.size(), remains)){
                result.add(message);
                if((! message.isRead()) && message.getTo().equals(user) )
                    read(message);
            }
        }
        return result;
    }

    /**
     * If receiver is registered in cash(e.i. online) message will be added to cash
     * otherwise to DB
     */
    public void addMessage(Message message){
        message.setDate(new Timestamp(System.currentTimeMillis()));
        int userToId = message.getTo().getId();
        if(messagesCash.registered(userToId)){
            messagesCash.put(message);
        }else {
            messageDAO.save(message);
        }
    }

    /**
     * Set messages from friend to user as read
     */
    public void read(int userId, int friendId){
        messagesCash.markRead(userId, friendId);
    }
    /**
     * Mark message as read
     */
    private void read(Message message){
        message.setRead();
        messageDAO.save(message);
    }

    /**
     * @return true if since last check new messages received and false otherwise.
     */
    public boolean hasNew(int userId){
        return messagesCash.hasNew(userId);
    }

    public void resetNew(int userId){
        messagesCash.resetNew(userId);
    }

}
