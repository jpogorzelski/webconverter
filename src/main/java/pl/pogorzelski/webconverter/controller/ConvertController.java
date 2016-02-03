package pl.pogorzelski.webconverter.controller;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pogorzelski.webconverter.domain.Converter;
import pl.pogorzelski.webconverter.domain.dto.SelectConverterForm;
import pl.pogorzelski.webconverter.domain.validator.SelectConverterFormValidator;
import pl.pogorzelski.webconverter.queue.ConvertTask;
import pl.pogorzelski.webconverter.queue.MyExecutorService;
import pl.pogorzelski.webconverter.service.ConverterService;
import pl.pogorzelski.webconverter.util.MailSendService;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

@Controller
public class ConvertController {

    private static final Logger LOG = LoggerFactory.getLogger(ConvertController.class);

    @Inject
    protected SelectConverterFormValidator selectConverterFormValidator;
    @Inject
    private ConverterService converterService;

    @Inject
    private MailSendService mailSendService;

    @Inject
    MyExecutorService executorService;

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
    @ResponseBody
    public void convert(@ModelAttribute("selectConverterForm") @Valid
                        SelectConverterForm selectConverterForm, HttpServletRequest req, HttpServletResponse res, BindingResult result)
            throws IOException {
        if (result.hasErrors()) {
            LOG.info("got errors...");
        }
        String sourceFormat = selectConverterForm.getSourceFormat();
        String targetFormat = selectConverterForm.getTargetFormat();

        MultipartFile file = selectConverterForm.getFile();
        Converter converter = converterService.getOneBySourceFormatAndTargetFormat(sourceFormat, targetFormat)
                .orElseThrow(() -> new NoSuchElementException(String.format("Converter %s -> %s not found",
                        sourceFormat, targetFormat)));
        // BaseConverter bc = ConverterUtils.getConverter(converter);
        String name = System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename();
        String destinationDir = name + "_output";
        if (!file.isEmpty()) {
            uploadFile(file, name);

            boolean prepareFilesAndDirs = prepareFilesAndDirs(name, destinationDir);
            if (prepareFilesAndDirs) {
                File sourceFile = new File(name);
                // File outputFile = convertFile(sourceFile);
                // assert bc != null;
                File outputFile = new File(System.getProperty("java.io.tmpdir") + File.separator + FilenameUtils.getBaseName(sourceFile.getName()) + "."
                        + targetFormat);
                //bc.convert(sourceFile,outputFile );


                /*
                try {
                    mailSendService.send(outputFile.getName());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }*/
                //  new ProducerConsumer().startEngine();

                // LOG.debug("successful conversion, class: " + bc.getClass().toString());

                // ExecutorService executor = Executors.newSingleThreadExecutor();

// You use your service repeatedly to submit your worker threads
// with inputs to process
                //Future<String> statusFuture = executor.submit(new ConvertTask(converter, sourceFile, outputFile));

// If interested, you retrieve the results

                try {
                    ConvertTask task = new ConvertTask(converter, sourceFile, outputFile);
                    executorService.add(task);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                //serveFile(req, res, outputFile.getAbsolutePath());
            }
        } else {
            LOG.info("You failed to upload " + name + " because the file was empty.");
        }

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
        LOG.info("{} File not exists", sourceFile.getName());

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

    private void serveFile(HttpServletRequest req, HttpServletResponse res, String finalOutName) throws IOException {
        ServletContext context = req.getServletContext();
        File downloadFile = new File(finalOutName);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        String mimeType = context.getMimeType(finalOutName);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        res.setContentType(mimeType);
        res.setContentLength((int) downloadFile.length());

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        res.setHeader(headerKey, headerValue);

        OutputStream outStream = res.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();
    }

}
