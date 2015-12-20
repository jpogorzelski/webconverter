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
import pl.pogorzelski.webconverter.convert.BaseConverter;
import pl.pogorzelski.webconverter.domain.Converter;
import pl.pogorzelski.webconverter.domain.dto.NewConverterForm;
import pl.pogorzelski.webconverter.domain.validator.NewConverterFormValidator;
import pl.pogorzelski.webconverter.service.ConverterService;

import javax.inject.Inject;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;

/**
 * Created by kuba on 12/15/15.
 */
@Controller
public class NewConverterController {
    private static final Logger LOG = LoggerFactory.getLogger(NewConverterController.class);
    private static final String PLUGIN_DIR = "plugins";
    private static final File ROOT_FOLDER = new File(PLUGIN_DIR);


    @Inject
    private NewConverterFormValidator newConverterFormValidator;


    @Inject
    private ConverterService converterService;

    private static boolean validConverter(Converter c) {
        Class<?> cls = loadClass(c);

        if (cls != null && BaseConverter.class.isAssignableFrom(cls)) {
            return true;
        }
        return false;
    }

    public static BaseConverter getConverter(Converter c) {
        Class<?> cls = loadClass(c);

        if (cls != null && BaseConverter.class.isAssignableFrom(cls)) {
            try {
                return (BaseConverter) cls.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Class<?> loadClass(Converter converter) {
        URLClassLoader classLoader = null;
        try {
            classLoader = URLClassLoader.newInstance(new URL[]{ROOT_FOLDER.toURI().toURL()});
        } catch (MalformedURLException e) {
            LOG.error(e.getMessage());
        }
        try {
            Class<?> cls = Class.forName(converter.getPackageName() + "." + converter.getClassName(), false,
                    classLoader);
            return cls;
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage());
        }

        return null;
    }

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(newConverterFormValidator);
    }

    @RequestMapping(value = "/newconverter", method = RequestMethod.POST)
    public String registerNewConverter(
            @Valid @ModelAttribute("form") NewConverterForm form, BindingResult bindingResult) {
        LOG.debug("Processing user create form={}, bindingResult={}", form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "newconverter";
        }

        String source = form.getSourceCode();

        Converter c = new Converter();
        c.setSourceCode(source);
        c.setClassName(getClassName(source));
        c.setPackageName(getPackageName(source));
        c.setSourceFormat(form.getSourceFormat());
        c.setTargetFormat(form.getTargetFormat());

        storeClassFile(c);

        if (validConverter(c)) {
            converterService.create(c);
        }

        // ok, redirect
        return "redirect:/test";
    }

    private String getPackageName(String source) {
        String tmp = StringUtils.substringAfter(source, "package ");
        return tmp.substring(0, tmp.indexOf(";"));
    }

    private String getClassName(String source) {
        String tmp = StringUtils.substringAfter(source, "class ");
        return tmp.substring(0, tmp.indexOf(" "));
    }

    private void storeClassFile(Converter form) {
        String packageNameSlashes = form.getPackageName().replaceAll("\\.", "/");

        File sourceFile = new File(ROOT_FOLDER, packageNameSlashes + "/" + form.getClassName() + ".java");
        sourceFile.getParentFile().mkdirs();
        try {
            Files.write(form.getSourceCode(), sourceFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        compileConverterFile(sourceFile);
    }

    private void compileConverterFile(File sourceFile) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, sourceFile.getPath());
    }

    @RequestMapping(value = "/newconverter", method = RequestMethod.GET)
    public ModelAndView renderConvert() {
        return new ModelAndView("newconverter", "form", new NewConverterForm());
    }

}

