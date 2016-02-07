package pl.pogorzelski.webconverter.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.pogorzelski.webconverter.domain.dto.ConvertForm;
import pl.pogorzelski.webconverter.domain.validator.ConvertFormValidator;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * Created by kuba on 11/23/15.
 */
@Controller
public class OldConvertController {

    private static final Logger LOG = LoggerFactory.getLogger(OldConvertController.class);

    @Inject
    protected ConvertFormValidator convertFormValidator;


    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(convertFormValidator);

    }

    @RequestMapping(value = "/oldconvert", method = RequestMethod.GET)
    public ModelAndView render() {
        return new ModelAndView("oldconvert", "form", new ConvertForm());
    }

    @RequestMapping(value = "/oldconvert", method = RequestMethod.POST)


    @ResponseBody
    public void handleFileUpload(@RequestParam("name") String name,
                                 @RequestParam("file") MultipartFile file, HttpServletRequest req,
                                 HttpServletResponse res) {
        if (!file.isEmpty()) {
            try {

                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();
                String finalOutName = "";
                try {
                    String sourceDir = name;//"C:/Documents/04-Request-Headers.pdf"; // Pdf files are read from this
                    // folder
                    String destinationDir = name + "_img";//"C:/Documents/Converted_PdfFiles_to_Image/"; // converted
                    // images from pdf document are saved here

                    File sourceFile = new File(sourceDir);
                    File destinationFile = new File(destinationDir);
                    if (!destinationFile.exists()) {
                        destinationFile.mkdir();
                        System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
                    }

                    if (sourceFile.exists()) {
                        System.out.println("Images copied to Folder: " + destinationFile.getName());
                        PDDocument document = PDDocument.load(sourceFile);
                        List<PDPage> list = document.getDocumentCatalog().getAllPages();
                        System.out.println("Total files to be converted -> " + list.size());


                        String fileName = sourceFile.getName().replace(".pdf", "");
                        int pageNumber = 1;
                        for (PDPage page : list) {
                            BufferedImage image = page.convertToImage();
                            File outputfile = new File(destinationDir + fileName + "_" + pageNumber + ".png");
                            finalOutName = outputfile.getAbsolutePath();
                            System.out.println("Image Created -> " + outputfile.getName());
                            ImageIO.write(image, "png", outputfile);
                            pageNumber++;
                        }
                        document.close();
                        System.out.println("Converted Images are saved at -> " + destinationFile.getAbsolutePath());

                    } else {
                        System.err.println(sourceFile.getName() + " FileEntry not exists");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // get absolute path of the application
                ServletContext context = req.getServletContext();
                String appPath = context.getRealPath("");
                System.out.println("appPath = " + appPath);

                // construct the complete absolute path of the file
                String fullPath = finalOutName;
                System.out.println("full: " + fullPath);
                File downloadFile = new File(fullPath);
                FileInputStream inputStream = new FileInputStream(downloadFile);
                String mimeType = context.getMimeType(fullPath);
                if (mimeType == null) {
                    // set to binary type if MIME mapping not found
                    mimeType = "application/octet-stream";
                }
                System.out.println("MIME type: " + mimeType);

                // set content attributes for the response
                res.setContentType(mimeType);
                res.setContentLength((int) downloadFile.length());

                // set headers for the response
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"",
                        downloadFile.getName());
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

                // return "You successfully uploaded " + name + "! ";
            } catch (Exception e) {
                // return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            // return "You failed to upload " + name + " because the file was empty.";
        }


    }
}
