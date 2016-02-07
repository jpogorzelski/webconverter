package pl.pogorzelski.webconverter.controller;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pogorzelski.webconverter.domain.Converter;
import pl.pogorzelski.webconverter.domain.dto.CurrentUser;
import pl.pogorzelski.webconverter.domain.dto.SelectConverterForm;
import pl.pogorzelski.webconverter.domain.validator.SelectConverterFormValidator;
import pl.pogorzelski.webconverter.queue.ConvertTask;
import pl.pogorzelski.webconverter.queue.MyExecutorService;
import pl.pogorzelski.webconverter.service.ConversionActionService;
import pl.pogorzelski.webconverter.service.ConverterService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

@Controller
public class ConvertController {

    public static final String FILE_TMP_DIR = System.getProperty("java.io.tmpdir") + File.separator;
    private static final Logger LOG = LoggerFactory.getLogger(ConvertController.class);

    @Inject
    protected SelectConverterFormValidator selectConverterFormValidator;

    @Inject
    private MyExecutorService executorService;

    @Inject
    private ConverterService converterService;

    @Inject
    private ConversionActionService conversionActionService;


    @InitBinder("selectConverterForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(selectConverterFormValidator);

    }

    @RequestMapping(value = "/convert", method = RequestMethod.GET)
    public String get(Model model) {
        SelectConverterForm form = new SelectConverterForm();
        model.addAttribute("selectConverterForm", form);
        LOG.debug("convert view. added selectConverterForm to model and returning convert page");
        return "convert";
    }


    @RequestMapping(value = "/convert", method = RequestMethod.POST)
    public String convert(@ModelAttribute("selectConverterForm") @Valid
                          SelectConverterForm selectConverterForm, BindingResult result)
            throws IOException {
        if (result.hasErrors()) {
            LOG.info("got errors...");
        }
        CurrentUser currentUser = getCurrentUser();
        String sourceFormat = selectConverterForm.getSourceFormat();
        String targetFormat = selectConverterForm.getTargetFormat();
        MultipartFile file = selectConverterForm.getFile();
        String name = FILE_TMP_DIR + file.getOriginalFilename();
        String destinationDir = name + "_output";

        Converter converter = converterService.getOneBySourceFormatAndTargetFormat(sourceFormat, targetFormat)
                .orElseThrow(() -> new NoSuchElementException(String.format("Converter %s -> %s not found",
                        sourceFormat, targetFormat)));
        if (!file.isEmpty()) {
            uploadFile(file, name);

            boolean prepareFilesAndDirs = prepareFilesAndDirs(name, destinationDir);
            if (prepareFilesAndDirs) {
                File sourceFile = new File(name);

                String targetFileName = FilenameUtils.getBaseName(sourceFile.getName()) + "." + targetFormat;
                File outputFile = new File(FILE_TMP_DIR + targetFileName);

                try {
                    ConvertTask task = new ConvertTask(converter, sourceFile, outputFile, currentUser.getUser(),
                            conversionActionService);
                    executorService.add(task);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            LOG.info("You failed to upload " + name + " because the file was empty.");
        }

        return "redirect:/tasks";
    }

    private CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CurrentUser) authentication.getPrincipal();
    }


    @RequestMapping(value = "/targets", method = RequestMethod.GET)
    @ResponseBody
    public Collection<String> findTargetFormatsForSource(
            @RequestParam(value = "sourceFormat", required = true) String sourceFormat) {
        LOG.debug("finding targets for sourceFormat " + sourceFormat);
        return this.converterService.getBySourceFormat(sourceFormat);
    }

    @RequestMapping(value = "/sources", method = RequestMethod.GET)
    @ResponseBody
    public Collection<String> findAllSourceFormats() {
        LOG.debug("finding all source formats");
        return this.converterService.getAllConverters();
    }

    private boolean prepareFilesAndDirs(String name, String destinationDir) {

        File sourceFile = new File(name);
        File destinationFile = new File(destinationDir);
        if (!destinationFile.exists()) {
            destinationFile.mkdir();
            LOG.info("Folder Created -> {}", destinationFile.getAbsolutePath());
        }
        if (sourceFile.exists()) {

            return true;
        }
        LOG.info("{} FileEntry not exists", sourceFile.getName());

        return false;
    }

    private void uploadFile(@RequestParam("file") MultipartFile file, String name) {
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name)));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }


    }


}
