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

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
    @Inject
    protected SelectConverterFormValidator selectConverterFormValidator;
    @Inject
    private ConverterService converterService;

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(selectConverterFormValidator);

    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String get(Model model) {
        SelectConverterForm form = new SelectConverterForm();
        model.addAttribute("selectConverterForm", form);
        LOG.debug("registering. added form to model and returning register page");
        return "test";
    }

    @RequestMapping(value = "/targets", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<String> findTargetFormatsForSource(
            @RequestParam(value = "sourceFormat", required = true) String sourceFormat) {
        LOG.debug("finding targets for sourceFormat " + sourceFormat);
        return this.converterService.getBySourceFormat(sourceFormat);
    }

    @RequestMapping(value = "/sources", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<String> findAllSourceFormats() {
        LOG.debug("finding all source formats");
        return this.converterService.getAllConverters();
    }

    @RequestMapping(value = "/convertNew", method = RequestMethod.POST)
    public
    @ResponseBody
    void convert(@ModelAttribute("selectConverterForm")
                 SelectConverterForm form, HttpServletRequest req, HttpServletResponse res, BindingResult result)
            throws IOException {

        if (result.hasErrors()) {
            LOG.debug("got errors...");
        }
        LOG.info("yo");
        String sourceFormat = form.getSourceFormat();
        String targetFormat = form.getTargetFormat();
        MultipartFile file = form.getFile();
        Converter converter = converterService.getOneBySourceFormatAndTargetFormat(sourceFormat, targetFormat)
                .orElseThrow(() -> new NoSuchElementException(String.format("Converter %s -> %s not found",
                        sourceFormat, targetFormat)));
        BaseConverter bc = NewConverterController.getConverter(converter);
        String name = System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename();
        String destinationDir = name + "_output";
        if (!file.isEmpty()) {
            uploadFile(file, name);

            boolean prepareFilesAndDirs = prepareFilesAndDirs(name, destinationDir);
            if (prepareFilesAndDirs) {
                File sourceFile = new File(name);
                File outputFile = convertFile(sourceFile);
                serveFile(req, res, outputFile.getAbsolutePath());
            }
        } else {
            LOG.info("You failed to upload " + name + " because the file was       // empty.");
        }

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
        String fullPath = finalOutName;
        LOG.info("full: " + fullPath);
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
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
        int bytesRead = -1;

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
