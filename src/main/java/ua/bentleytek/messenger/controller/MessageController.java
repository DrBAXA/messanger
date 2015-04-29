package ua.bentleytek.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.bentleytek.messenger.entity.Message;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.MessageService;
import ua.bentleytek.messenger.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Message>> getMessages(Principal user,
                                                     @RequestParam(value = "first", required = false, defaultValue = "0") int first,
                                                     @RequestParam(value = "friendId", required = true) int friendId)
    {
        if(user != null) {
            User from = userService.getUser(user.getName());
            User to = userService.getUser(friendId);
            Iterable<Message> messages = messageService.getMessages(from, to, first, MessageService.DEFAULT_PAGE_COUNT);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        }else{
            return new ResponseEntity<Iterable<Message>>(HttpStatus.FORBIDDEN);
        }
    }
}
