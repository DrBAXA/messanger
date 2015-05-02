package ua.bentleytek.messenger.interceptor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ua.bentleytek.messenger.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor extends HandlerInterceptorAdapter {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private UserService userService;

    @Override
    public void postHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("Adding user name after controller " + handler.toString());
        Object uncastUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if((uncastUser instanceof User) && (modelAndView != null)){
            String name = ((User)uncastUser).getUsername();
            modelAndView.getModelMap().addAttribute("user", name);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if(userService == null){
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        }
        Object uncastUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(uncastUser instanceof User){
            String name = ((User)uncastUser).getUsername();
            userService.setLastVisit(name);
        }
        return true;
    }
}
