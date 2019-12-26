package pl.pogorzelski.webconverter.service;

import pl.pogorzelski.webconverter.domain.SMSToken;
import pl.pogorzelski.webconverter.domain.User;

import java.util.List;

/**
 * @author Kuba
 */
public interface SMSTokenService {

    List<SMSToken> getAllTokens();

    SMSToken getByToken(String token);

    List<SMSToken> getAllByUserOrderByCreateDate(User user);

    boolean verifyToken(SMSToken smsToken);

    void save(SMSToken smsToken);


}
