package pl.pogorzelski.webconverter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.pogorzelski.webconverter.domain.dto.ConvertForm;
import pl.pogorzelski.webconverter.domain.validator.ConvertFormValidator;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kuba on 11/23/15.
 */
@Controller
public class ConvertController {

    private static final Logger LOG = LoggerFactory.getLogger(ConvertController.class);

    @Inject
    protected ConvertFormValidator convertFormValidator;


    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(convertFormValidator);

    }

    @RequestMapping(value = "/convert", method = RequestMethod.GET)
    public ModelAndView render() {
        return new ModelAndView("convert", "form", new ConvertForm());
    }

    @RequestMapping(value = "/convert", method = RequestMethod.POST)

    public
    @ResponseBody
    void handleFileUpload(@RequestParam("name") String name,
                          @RequestParam("file") MultipartFile file, HttpServletRequest req, HttpServletResponse res) {


    }


}
