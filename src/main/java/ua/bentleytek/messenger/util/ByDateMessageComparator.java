package ua.bentleytek.messenger.util;

import ua.bentleytek.messenger.entity.Message;

import java.util.Comparator;

public class ByDateMessageComparator implements Comparator<Message>{

    @Override
    public int compare(Message message1, Message message2) {
        if(message1.getDate().after(message2.getDate()))
            return -1;
        if(message1.getDate().before(message2.getDate()))
            return 1;
        return 0;
    }
}
