package pl.pogorzelski.webconverter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public String getHomePage() {
        LOGGER.debug("Getting home page");
        return "home";
    }

    @RequestMapping("/403")
    public String get403() {
        LOGGER.info("GOT 403...");
        return "403";
    }

}
