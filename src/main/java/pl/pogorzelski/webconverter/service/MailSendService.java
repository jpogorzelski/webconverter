package pl.pogorzelski.webconverter.service;

import pl.pogorzelski.webconverter.domain.FileEntry;
import pl.pogorzelski.webconverter.domain.User;

/**
 * @author Kuba
 */
public interface MailSendService {

    void send(FileEntry fileEntry, User user);
}
