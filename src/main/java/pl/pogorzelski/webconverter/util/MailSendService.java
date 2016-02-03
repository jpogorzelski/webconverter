package pl.pogorzelski.webconverter.util;

import javax.mail.MessagingException;

/**
 * @author Kuba
 */
public interface MailSendService {

    void send(String fileName) throws MessagingException;
}
