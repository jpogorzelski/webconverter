package pl.pogorzelski.webconverter.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import pl.pogorzelski.webconverter.convert.BaseConverter;
import pl.pogorzelski.webconverter.domain.FileEntry;
import pl.pogorzelski.webconverter.domain.User;
import pl.pogorzelski.webconverter.service.ConversionActionService;
import pl.pogorzelski.webconverter.service.FileService;
import pl.pogorzelski.webconverter.service.MailSendService;
import pl.pogorzelski.webconverter.service.UserService;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

/**
 * @author Kuba
 */
@Service
public class ConversionActionServiceImpl implements ConversionActionService {

    @Inject
    private FileService fileService;

    @Inject
    private MailSendService mailSendService;

    @Inject
    private UserService userService;


    @Override
    public void convert(BaseConverter converter, File source, File target, User user) {
        try {

            converter.convert(source, target);
            FileEntry fileEntry = new FileEntry();
            fileEntry.setFile(target);
            fileEntry.setName(target.getName());
            fileEntry.setExtension(FilenameUtils.getExtension(target.getName()));
            fileEntry.setOwner(user);
            fileService.save(fileEntry);
            userService.incrementCurrentConversionCount(user);
            mailSendService.send(fileEntry, user);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
