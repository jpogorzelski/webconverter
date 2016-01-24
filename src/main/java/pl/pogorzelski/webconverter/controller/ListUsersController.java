package pl.pogorzelski.webconverter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.pogorzelski.webconverter.service.user.UserService;

import javax.inject.Inject;

@Controller
public class ListUsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListUsersController.class);
    private final UserService userService;

    @Inject
    public ListUsersController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/users")
    public ModelAndView getUsersPage() {
        LOGGER.debug("Getting users page");
        return new ModelAndView("users", "users", userService.getAllUsers());
    }


}