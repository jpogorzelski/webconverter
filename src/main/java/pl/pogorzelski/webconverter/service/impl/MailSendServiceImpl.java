package pl.pogorzelski.webconverter.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.pogorzelski.webconverter.domain.FileEntry;
import pl.pogorzelski.webconverter.domain.User;
import pl.pogorzelski.webconverter.service.MailSendService;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Kuba
 */
@Service
public class MailSendServiceImpl implements MailSendService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    private JavaMailSender javaMailSender;

    @Value("${mail.subject}")
    private String subject;

    @Value("${mail.body}")
    private String body;

    @Value("${download.url}")
    private String downloadURL;

    @Override
    public void send(FileEntry fileEntry, User user) {
        try {
            fillPlaceholders(user, fileEntry);
            MimeMessage mimeMessage = prepareMimeMessage(user);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private MimeMessage prepareMimeMessage(User user) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject(this.subject);
        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setText(body, true);
        return mimeMessage;
    }

    private void fillPlaceholders(User user, FileEntry fileEntry) {
        subject = subject.replace("{file}", fileEntry.getName());
        body = body.replace("{user}", user.getEmail())
                .replace("{file}", fileEntry.getName())
                .replace("{download}", StringUtils.join(downloadURL, user.getId(), "/", fileEntry.getMd5Hash()));
    }
}
