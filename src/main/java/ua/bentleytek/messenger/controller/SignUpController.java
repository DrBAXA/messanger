package ua.bentleytek.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String signUpPage(ModelMap map){
        map.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registration( @Valid User user,
                                BindingResult errors){
        if(errors.hasErrors()){
            return "signup";
        }
        userService.addUser(user);
        return "redirect:/login";
    }
}
