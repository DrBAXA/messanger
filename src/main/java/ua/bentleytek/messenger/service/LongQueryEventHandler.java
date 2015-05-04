package ua.bentleytek.messenger.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LongQueryEventHandler {
    private Map<Integer, Event> eventMap = new ConcurrentHashMap<>();

    public void setEvent(int userId){
        if(eventMap.containsKey(userId)){
            Event monitor = eventMap.get(userId);
            synchronized (monitor){
                monitor.notifyAll();
            }
        }
    }

    public void subscribe(int userId, Event event){
        eventMap.put(userId, event);
    }
}

