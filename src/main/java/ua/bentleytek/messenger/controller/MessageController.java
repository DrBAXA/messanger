package ua.bentleytek.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Map;

import static ua.bentleytek.messenger.service.EventType.*;

@Controller
@RequestMapping("/messages")
public class MessageController {

    public static final int LONG_QUERY_TIMEOUT = 1000000;
    @Autowired
    private MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    LongQueryEventHandler eventHandler;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Message>> getMessages(Principal principal,
                                                     @RequestParam("first") int first,
                                                     @RequestParam(value = "count", required = false, defaultValue = "10") int count,
                                                     @RequestParam(value = "friendId") int friendId)

    {
        if(principal != null) {
            User user = userService.getUser(principal.getName());
            User friend = userService.getUser(friendId);
            Iterable<Message> messages = messageService.getMessages(user, friend, first, count);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addMessage(@RequestBody Message message, Principal user){
        if(user != null) {
            message.setFrom(userService.getUser(user.getName()));
            message.setDate(new Timestamp(System.currentTimeMillis()));
            messageService.addMessage(message);
            eventHandler.setEvent(message.getTo().getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //Long query
    @RequestMapping("/unread")
    public ResponseEntity<Map<Integer, Integer>> checkNewMessages(Principal principal){
        if(principal != null) {
            User user = userService.getUser(principal.getName());
            Map<Integer, Integer> result;
            if(messageService.hasNew(user.getId())) {
                result = messageService.getUnreadCountBySender(user);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else{
                Event event = new Event(NEW_MESSAGE);
                synchronized (event){
                    eventHandler.subscribe(user.getId(), event);
                    try {
                        event.wait(LONG_QUERY_TIMEOUT);
                        result = messageService.getUnreadCountBySender(user);
                        return new ResponseEntity<>(result, HttpStatus.OK);
                    } catch (InterruptedException ie) {
                        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
                    }
                }
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping("/read/{friendId}")
    public ResponseEntity<Void> markAsRead(Principal principal,
                                           @PathVariable("friendId") int friendId){
        if(principal != null){
            User user = userService.getUser(principal.getName());
            messageService.read(user.getId(), friendId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
