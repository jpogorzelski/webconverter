package pl.pogorzelski.webconverter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pogorzelski.webconverter.domain.dto.RegisterForm;
import pl.pogorzelski.webconverter.domain.validator.UserCreateFormValidator;
import pl.pogorzelski.webconverter.service.UserService;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * @author Kuba
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
    private final UserService userService;
    private final UserCreateFormValidator userCreateFormValidator;

    @Inject
    public RegisterController(UserService userService, UserCreateFormValidator userCreateFormValidator) {
        this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
    }

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getRegisterPage() {
        LOGGER.debug("Getting register form");
        return new ModelAndView("register", "form", new RegisterForm());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String handleRegisterForm(@Valid @ModelAttribute("form") RegisterForm form, BindingResult
            bindingResult) {
        LOGGER.debug("Processing register form={}, bindingResult={}", form, bindingResult);
        if (bindingResult.hasErrors()) {
            // failed validation
            return "register";
        }
        try {
            userService.create(form);
        } catch (DataIntegrityViolationException e) {
            // probably email already exists - very rare case when multiple admins are adding same user
            // at the same time and form validation has passed for more than one of them.
            LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate email", e);
            bindingResult.reject("email.exists", "Email already exists");
            return "register";
        }
        // ok, redirect
        return "redirect:/login";
    }
}
