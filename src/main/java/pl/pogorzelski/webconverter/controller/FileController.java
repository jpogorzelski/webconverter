package pl.pogorzelski.webconverter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.pogorzelski.webconverter.domain.FileEntry;
import pl.pogorzelski.webconverter.service.FileService;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Kuba
 */
@Controller
public class FileController {

    @Inject
    private FileService fileService;

    @RequestMapping("/files")
    public ModelAndView getAllFiles() {
        ModelAndView modelAndView = new ModelAndView("files");
        List<FileEntry> files = fileService.getAllFiles();
        modelAndView.addObject("files", files);
        return modelAndView;

    }
}
