package pl.pogorzelski.webconverter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.pogorzelski.webconverter.domain.SMSToken;
import pl.pogorzelski.webconverter.domain.User;
import pl.pogorzelski.webconverter.service.SMSTokenService;
import pl.pogorzelski.webconverter.service.TwillioServiceSMS;
import pl.pogorzelski.webconverter.service.UserService;
import pl.pogorzelski.webconverter.util.Message;
import pl.pogorzelski.webconverter.util.SecurityUtils;

import javax.inject.Inject;

/**
 * @author Kuba
 */
@Controller
public class UnlockController {

    private static final String VALIDATION_ERROR = "Wrong number format. Expected 11 digits with leading '+', e.g. " +
            "+48111222333";
    @Inject
    private TwillioServiceSMS serviceSMS;

    @Inject
    private UserService userService;

    @Inject
    private SMSTokenService smsTokenService;

    @RequestMapping(value = "/unlock", method = RequestMethod.GET)
    public ModelAndView getView() {
        ModelAndView modelAndView = new ModelAndView("unlock");
        User currentUser = SecurityUtils.getLoggedUser();
        if (currentUser != null) {
            modelAndView.addObject("phoneNo", currentUser.getPhoneNo());
        }
        modelAndView.addObject("step1", true);
        return modelAndView;
    }


    @RequestMapping(value = "/unlock2", method = RequestMethod.GET)
    public ModelAndView getVerifyView() {
        ModelAndView modelAndView = new ModelAndView("unlock");
        modelAndView.addObject("step2", true);
        return modelAndView;
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    public ModelAndView sendSMS(@RequestParam String phoneNo) {
        ModelAndView modelAndView = new ModelAndView("unlock");
        Message message;
        if (phoneNo.matches("\\+\\d{11}")) {
            phoneNo = phoneNo.trim();

            User currentUser = SecurityUtils.getLoggedUser();
            currentUser.setPhoneNo(phoneNo);
            userService.save(currentUser);

            SMSToken smsToken = new SMSToken(currentUser);
            smsTokenService.save(smsToken);

            serviceSMS.sendSMS(phoneNo, smsToken.getToken());

            modelAndView.addObject("step2", true);
            message = new Message("info", "Enter code that you received on your phone");
        } else {
            message = new Message("danger", VALIDATION_ERROR);
        }
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @RequestMapping(value = "/unlock2", method = RequestMethod.POST)
    public String unlock(@RequestParam String code, Model model) {

        SMSToken token = smsTokenService.getByToken(code.trim());
        if (token != null && smsTokenService.verifyToken(token)) {
            User currentUser = SecurityUtils.getLoggedUser();
            currentUser.setCurrentConversionCount(0);
            userService.save(currentUser);

            Message message = new Message("success", "Accese unlocked!");
            model.addAttribute("message", message);
            return "home";

        } else {
            Message message = new Message("danger", "Wrong token!");
            model.addAttribute("step2", true);
            model.addAttribute("message", message);
            return "unlock";
        }


    }
}
