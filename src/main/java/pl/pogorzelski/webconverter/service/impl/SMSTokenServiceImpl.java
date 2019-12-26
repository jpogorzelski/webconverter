package pl.pogorzelski.webconverter.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pogorzelski.webconverter.domain.SMSToken;
import pl.pogorzelski.webconverter.domain.User;
import pl.pogorzelski.webconverter.repository.SMSTokenRepository;
import pl.pogorzelski.webconverter.service.SMSTokenService;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Kuba
 */
@Service
@Transactional
public class SMSTokenServiceImpl implements SMSTokenService {

    private Logger log = LoggerFactory.getLogger(SMSTokenServiceImpl.class);

    @Inject
    private SMSTokenRepository tokenRepository;

    @Override
    public List<SMSToken> getAllTokens() {
        return tokenRepository.findAll();
    }

    @Override
    public SMSToken getByToken(String token) {
        return tokenRepository.findByToken(token).get();
    }

    @Override
    public boolean verifyToken(SMSToken smsToken) {
        final List<SMSToken> tokenList = getAllByUserOrderByCreateDate(smsToken.getUser());
        tokenList.subList(1, tokenList.size()).forEach(t -> {
            t.setExpired(true);
            //save(t);
            log.info(t.toString());
        });

        if (smsToken.isExpired()) {
            final String message = "Token " + smsToken.getToken() + " is expired!";
            log.error(message);
            return false;
        }
        smsToken.setExpired(true);
        return true;
    }

    @Override
    public List<SMSToken> getAllByUserOrderByCreateDate(User user) {
        return tokenRepository.findAllByUserOrderByCreateDateDesc(user);
    }

    @Override
    public void save(SMSToken smsToken) {
        tokenRepository.save(smsToken);
    }


}
