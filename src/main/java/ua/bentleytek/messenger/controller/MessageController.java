package ua.bentleytek.messenger.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.Event;
import ua.bentleytek.messenger.service.LongQueryEventService;
import ua.bentleytek.messenger.service.MessageService;
import ua.bentleytek.messenger.service.UserService;

import java.security.Principal;
import java.util.Map;

import static ua.bentleytek.messenger.service.EventType.NEW_MESSAGE;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final Logger logger = Logger.getLogger(this.getClass());

    public static final int LONG_QUERY_TIMEOUT = 1000000;
    @Autowired
    private MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    LongQueryEventService eventHandler;

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
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!unread");
        if(principal != null) {
            User user = userService.getUser(principal.getName());
            if(messageService.hasNew(user.getId())) {
                return new ResponseEntity<>(messageService.getUnreadCountBySender(user), HttpStatus.OK);
            }else{
                Event event = new Event(NEW_MESSAGE);
                synchronized (event){
                    eventHandler.subscribe(user.getId(), event);
                    try {
                        event.wait(LONG_QUERY_TIMEOUT);
                        messageService.resetNew(user.getId());
                        return new ResponseEntity<>(messageService.getUnreadCountBySender(user), HttpStatus.OK);
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
