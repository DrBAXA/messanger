package ua.bentleytek.messenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.bentleytek.messenger.dao.MessageDAO;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;

/**
 *
 */
@Service
public class MessageService {
    public static final int DEFAULT_PAGE_COUNT = 20;

    @Autowired
    private MessageDAO messageDAO;

    public Iterable<Message> getMessages(User from, User to){
        return getMessages(from, to, 0);
    }

    public Iterable<Message> getMessages(User from, User to, int first){
        return getMessages(from, to, first, DEFAULT_PAGE_COUNT);
    }

    public Iterable<Message> getMessages(User from, User to, int first, int count){
        return messageDAO.getByUser(from, to, first, count);
    }

}
