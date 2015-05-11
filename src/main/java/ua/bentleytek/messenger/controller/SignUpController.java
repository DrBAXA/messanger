package ua.bentleytek.messenger.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.bentleytek.messenger.entity.User;
import ua.bentleytek.messenger.service.FileService;
import ua.bentleytek.messenger.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    UserService userService;
    @Autowired
    FileService fileService;
    @Lazy
    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(method = RequestMethod.GET)
    public String signUpPage(ModelMap map){
        map.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registration( @Valid User user,
                                BindingResult errors,
                                @RequestParam MultipartFile image){
        if(errors.hasErrors()){
            return "signup";
        }
        String photoFileName = fileService.saveFile(image, user.getName());
        user.setPhoto(photoFileName);

        String rawPassword = user.getPassword();

        userService.addUser(user);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getName(), rawPassword);
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        return "redirect:/";
    }
}
