package pl.pogorzelski.webconverter.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pogorzelski.webconverter.convert.BaseConverter;
import pl.pogorzelski.webconverter.domain.Converter;
import pl.pogorzelski.webconverter.domain.dto.SelectConverterForm;
import pl.pogorzelski.webconverter.domain.validator.SelectConverterFormValidator;
import pl.pogorzelski.webconverter.service.ConverterService;
import pl.pogorzelski.webconverter.util.ConverterUtils;
import pl.pogorzelski.webconverter.util.MailSendService;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class ConvertController {

    private static final Logger LOG = LoggerFactory.getLogger(ConvertController.class);

    @Inject
    protected SelectConverterFormValidator selectConverterFormValidator;
    @Inject
    private ConverterService converterService;

    @Inject
    private MailSendService mailSendService;

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
        LOG.info("yo");
        String sourceFormat = selectConverterForm.getSourceFormat();
        String targetFormat = selectConverterForm.getTargetFormat();

        MultipartFile file = selectConverterForm.getFile();
        Converter converter = converterService.getOneBySourceFormatAndTargetFormat(sourceFormat, targetFormat)
                .orElseThrow(() -> new NoSuchElementException(String.format("Converter %s -> %s not found",
                        sourceFormat, targetFormat)));
        BaseConverter bc = ConverterUtils.getConverter(converter);
        String name = System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename();
        String destinationDir = name + "_output";
        if (!file.isEmpty()) {
            uploadFile(file, name);

            boolean prepareFilesAndDirs = prepareFilesAndDirs(name, destinationDir);
            if (prepareFilesAndDirs) {
                File sourceFile = new File(name);
                // File outputFile = convertFile(sourceFile);
                assert bc != null;
                File outputFile = bc.convert(sourceFile);
                serveFile(req, res, outputFile.getAbsolutePath());
                try {
                    mailSendService.send(outputFile.getName());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                LOG.debug("successful conversion, class: " + bc.getClass().toString());
            }
        } else {
            LOG.info("You failed to upload " + name + " because the file was       // empty.");
        }

        LOG.debug("successful conversion");
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
        // get absolute path of the application
        ServletContext context = req.getServletContext();
        String appPath = context.getRealPath("");
        LOG.info("appPath = " + appPath);

        // construct the complete absolute path of the file
        LOG.info("full: " + finalOutName);
        File downloadFile = new File(finalOutName);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        // get MIME type of the file
        String mimeType = context.getMimeType(finalOutName);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        LOG.info("MIME type: " + mimeType);

        // set content attributes for the response
        res.setContentType(mimeType);
        res.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        res.setHeader(headerKey, headerValue);

        // get output stream of the response
        OutputStream outStream = res.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();
    }

    private File convertFile(File sourceFile)
            throws IOException {

        PDDocument document = PDDocument.load(sourceFile);
        @SuppressWarnings("unchecked")
        List<PDPage> list = document.getDocumentCatalog().getAllPages();
        PDPage page = list.get(0);
        LOG.info("Total files to be converted -> {}", list.size());

        String fileName = sourceFile.getName().replace(".pdf", "");
        int pageNumber = 1;
        BufferedImage image = page.convertToImage();
        File outputFile = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName + "_" +
                pageNumber + ".png");

        LOG.info("Image Created -> {}", outputFile.getName());
        ImageIO.write(image, "png", outputFile);
        document.close();

        return outputFile;
    }


}
