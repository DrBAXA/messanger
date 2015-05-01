package ua.bentleytek.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.MessageDAO;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class MessageService {
    public static final int DEFAULT_PAGE_COUNT = 10;

    @Autowired
    private MessageDAO messageDAO;

    public Map<Integer, Integer> getUnread(User user){
        Iterable<Message> messages = messageDAO.getUnread(user);
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

    public Iterable<Message> getMessages(User user, User friend, boolean setRead){
        return getMessages(user, friend, 0, setRead);
    }

    public Iterable<Message> getMessages(User user, User friend, int first, boolean setRead){
        return getMessages(user, friend, first, DEFAULT_PAGE_COUNT, setRead);
    }

    public Iterable<Message> getMessages(User user, User friend, int first, int count, boolean setRead){
        Iterable<Message> messages = messageDAO.getByUser(user, friend, first, count);
        for(Message message : messages){
            if(setRead && (! message.isRead()) && message.getTo().equals(user) )
                read(message);
        }
        return messages;
    }

    public void addMessage(Message message){
        messageDAO.save(message);
    }

    public void read(Message message){
        message.setRead(true);
        messageDAO.save(message);
    }

}
