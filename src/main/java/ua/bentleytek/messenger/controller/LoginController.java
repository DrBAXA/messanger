package ua.bentleytek.messenger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @RequestMapping(value = "/login")
    public String login(@RequestParam(value = "error", required = false, defaultValue = "0") int error, ModelMap model) {
        if (error != 0) {
            model.addAttribute("error","error");
        }
        return "login";
    }
}
