package ua.bentleytek.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.MessageService;
import ua.bentleytek.messenger.service.UserService;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Map;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    UserService userService;

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
        message.setFrom(userService.getUser(user.getName()));
        message.setDate(new Timestamp(System.currentTimeMillis()));
        messageService.addMessage(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/unread")
    public ResponseEntity<Map<Integer, Integer>> checkNewMessages(Principal principal){
        if(principal != null) {
            User user = userService.getUser(principal.getName());
            return new ResponseEntity<>(messageService.getUnread(user), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping("/read")
    public ResponseEntity<Void> markAsRead(Principal principal,
                                           @)
}
