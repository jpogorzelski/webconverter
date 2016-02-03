package pl.pogorzelski.webconverter.util;

import javax.inject.Inject;
import javax.mail.MessagingException;

/**
 * @author Kuba
 */
public class MailSendServiceUtil {


    private static MailSendService mailService;

    @Inject
    public void setMailService(MailSendService mailSendService){
        MailSendServiceUtil.mailService = mailSendService;
    }

    public static void send(String fileName){
        try {
            MailSendServiceUtil.mailService.send(fileName);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
