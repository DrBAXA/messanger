package ua.bentleytek.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/friends")
public class FriendsController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<User>> getFriends(Principal user){
        return new ResponseEntity<Iterable<User>>(userService.getFriends(user.getName()), HttpStatus.OK);
    }

    @RequestMapping("/online")
    public ResponseEntity<Set<Integer>> checkOnline(Principal principal){
        if(principal != null) {
            return new ResponseEntity<>(userService.getOnlineFriends(principal.getName()), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
