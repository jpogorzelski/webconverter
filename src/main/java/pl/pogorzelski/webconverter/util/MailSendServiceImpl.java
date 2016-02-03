package pl.pogorzelski.webconverter.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pogorzelski.webconverter.domain.User;
import pl.pogorzelski.webconverter.domain.dto.CurrentUser;

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

    @Override
    public void send(String fileName) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        User u = ((CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        fillPlaceholders(u, fileName);
        mimeMessageHelper.setSubject(this.subject);
        mimeMessageHelper.setTo(u.getEmail());
        mimeMessageHelper.setText(body, true);
        javaMailSender.send(mimeMessage);


    }

    private void fillPlaceholders(User user, String fileName) {

        body = body.replace("{user}", user.getEmail()).replace("{file}", fileName).replace("{download}", "http://google.pl");
        subject = subject.replace("{file}", fileName);
    }
}
