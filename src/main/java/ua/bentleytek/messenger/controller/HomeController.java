package ua.bentleytek.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.bentleytek.messenger.service.UserService;

import java.io.IOException;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

	@RequestMapping(value="/")
	public String homePage(ModelMap modelMap, Principal user) throws IOException{
		return "home";
	}
}
