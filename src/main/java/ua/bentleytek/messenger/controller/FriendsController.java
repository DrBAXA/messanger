package ua.bentleytek.messenger.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/friends")
public class FriendsController {

    public static final String LOG_REQUEST = "Request received";

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<User>> getFriends(Principal principal) {
        if (principal != null) {
            return new ResponseEntity<Iterable<User>>(userService.getFriends(principal.getName()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/online", method = RequestMethod.GET)
    public ResponseEntity<Set<Integer>> checkOnline(Principal principal) {
        if (principal != null) {
            return new ResponseEntity<>(userService.getOnlineFriends(principal.getName()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<User> find(@RequestParam String nameOrEmail) {
        User user = userService.find(nameOrEmail);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/invitations/{id}", method = RequestMethod.POST)
    public ResponseEntity<Void> addInvitation(Principal principal,
                                              @PathVariable("id") int userId) {
        if (userService.addInvitation(principal.getName(), userId)) {
            System.out.println("invitation!!!!!!!!!!!!!!");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping("/invitations")
    public ResponseEntity<Set<User>> getInvitations(Principal principal){
        if(principal != null){
            Set<User> invitations = userService.getUser(principal.getName()).getFriendInvitations();
            return new ResponseEntity<>(invitations, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/invitations/{id}/accept", method = RequestMethod.POST)
    public ResponseEntity<Void> acceptInvitation(Principal principal,
                                                 @PathVariable("id") int invitorId)
    {
        if(principal != null){
            boolean result = userService.acceptInvitation(principal.getName(), invitorId);
            if(result){
                System.out.println("invitation accepted!!!!!!!!!!!!!!");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/invitations/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> rejectInvitation(Principal principal,
                                                 @PathVariable("id") int invitorId)
    {
        if(principal != null){
            boolean result = userService.rejectInvitation(principal.getName(), invitorId);
            if(result){
                System.out.println("invitation rejected!!!!!!!!!!!!!!");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
