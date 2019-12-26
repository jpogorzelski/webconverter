package pl.pogorzelski.webconverter.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(ConversionActionServiceImpl.class);

    @Override
    public void convert(BaseConverter converter, File source, File target, User user) {
        try {

            converter.convert(source, target);
            FileEntry fileEntry = new FileEntry();
            fileEntry.setFile(target);
            String fileName = target.getName();
            fileEntry.setName(fileName);
            String extension = FilenameUtils.getExtension(fileName);
            fileEntry.setExtension(extension);
            fileEntry.setOwner(user);
            String strToHash = fileName + System.currentTimeMillis();
            String md5Hash = DigestUtils.md5Hex(strToHash);
            logger.info("## HASH: {}", md5Hash);
            fileEntry.setMd5Hash(md5Hash);
            fileService.save(fileEntry);
            userService.incrementCurrentConversionCount(user);
            mailSendService.send(fileEntry, user);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
