package ua.bentleytek.messenger.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.UserService;

import java.io.IOException;
import java.security.Principal;

@Controller
public class HomeController {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    Environment env;

    @Autowired
    UserService userService;

	@RequestMapping(value="/")
	public String homePage(Principal principal, ModelMap modelMap) throws IOException{
        //add user name to page
        if(principal != null) {
            User user = userService.getUser(principal.getName());
            modelMap.addAttribute("user", principal.getName());
            int invitationsCount = user.getFriendInvitations().size();
            if(invitationsCount > 0)
                modelMap.addAttribute("invitations", invitationsCount);
        }
        //add timeout property
        modelMap.addAttribute("timeout", env.getProperty("timeout.refresh", Integer.class));
		return "home";
	}
}
