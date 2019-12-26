package pl.pogorzelski.webconverter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.pogorzelski.webconverter.domain.FileEntry;
import pl.pogorzelski.webconverter.exception.FileNotFoundException;
import pl.pogorzelski.webconverter.service.FileService;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * @author Kuba
 */
@RequestMapping("/download")
@Controller
public class DownloadController {

    @Inject
    private FileService fileService;

    private Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @RequestMapping("/{md5Hash}")
    public void download(@PathVariable String md5Hash, HttpServletRequest req, HttpServletResponse res) {
        FileEntry fileEntry = fileService.getFileByMd5Hash(md5Hash).orElseThrow(() -> new FileNotFoundException
                (String.format("File with md5Hash %s not found", md5Hash)));

        try {
            serveFile(req, res, fileEntry.getFile());
        } catch (IOException e) {
            logger.error("Error downloading file: {}", fileEntry.getName());
            e.printStackTrace();
        }


    }

    private void serveFile(HttpServletRequest req, HttpServletResponse res, File file) throws IOException {
        ServletContext context = req.getServletContext();

        FileInputStream inputStream = new FileInputStream(file);

        String mimeType = context.getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        res.setContentType(mimeType);
        res.setContentLength((int) file.length());

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
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
