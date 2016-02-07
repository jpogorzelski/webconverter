package pl.pogorzelski.webconverter.controller;

import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pl.pogorzelski.webconverter.domain.Converter;
import pl.pogorzelski.webconverter.domain.dto.NewConverterForm;
import pl.pogorzelski.webconverter.domain.validator.NewConverterFormValidator;
import pl.pogorzelski.webconverter.service.ConverterService;
import pl.pogorzelski.webconverter.util.Constants;
import pl.pogorzelski.webconverter.util.ConverterUtils;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author kuba
 */
@Controller
public class NewConverterController {
    public static final String JAVA = ".java";
    private static final Logger LOG = LoggerFactory.getLogger(NewConverterController.class);
    @Inject
    private NewConverterFormValidator newConverterFormValidator;


    @Inject
    private ConverterService converterService;


    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(newConverterFormValidator);
    }


    @RequestMapping(value = "/newconverter", method = RequestMethod.GET)
    public ModelAndView renderConvert() {
        return new ModelAndView("newconverter", "form", new NewConverterForm());
    }

    @RequestMapping(value = "/newconverter", method = RequestMethod.POST)
    public String registerNewConverter(
            @Valid @ModelAttribute("form") NewConverterForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "newconverter";
        }

        String source = form.getSourceCode();
        String className = getClassName(source);
        String packageName = getPackageName(source);
        if (className != null || packageName != null) {
            Converter c = new Converter();
            c.setSourceCode(source);
            c.setClassName(className);
            c.setPackageName(packageName);
            c.setSourceFormat(form.getSourceFormat());
            c.setTargetFormat(form.getTargetFormat());

            try {
                storeClassFile(c);
                if (ConverterUtils.validConverter(c)) {
                    converterService.create(c);
                    LOG.info("Success adding converter");
                    return "redirect:/convert";
                }
            } catch (CompilationException e) {
                LOG.error(e.toString());
                bindingResult.rejectValue("sourceCode", "error.sourceCode", "Error in source");
            }
        }
        bindingResult.rejectValue("sourceCode", "wrong.sourceCode", "Wrong/Error in source");
        return "newconverter";
    }


    private String getPackageName(String source) {
        String tmp = StringUtils.substringAfter(source, "package ");
        if (StringUtils.isNotBlank(tmp))
            return tmp.substring(0, tmp.indexOf(";"));
        return null;
    }

    private String getClassName(String source) {
        String tmp = StringUtils.substringAfter(source, "class ");
        if (StringUtils.isNotBlank(tmp))
            return tmp.substring(0, tmp.indexOf(" "));
        return null;
    }

    private void storeClassFile(Converter form) throws CompilationException {
        String packageNameSlashes = form.getPackageName().replaceAll("\\.", "/");

        File sourceFile = new File(Constants.ROOT_FOLDER, packageNameSlashes + File.separator + form.getClassName()
                + JAVA);
        sourceFile.getParentFile().mkdirs();
        try {
            Files.write(form.getSourceCode(), sourceFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        ConverterUtils.compileConverterFile(sourceFile);
    }


}

