package pl.pogorzelski.webconverter.service.impl;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.pogorzelski.webconverter.service.TwillioServiceSMS;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kuba
 */
@Service
public class TwillioServiceSMSImpl implements TwillioServiceSMS {

    private Logger log = LoggerFactory.getLogger(TwillioServiceSMSImpl.class);

    @Value("${twillio.sid}")
    private String SID;
    @Value("${twillio.auth.token}")
    private String authToken;

    @Value("${twillio.phone.no}")
    private String phoneNo;

    @Value("${twillio.message.body}")
    private String messageBody;



    @Override
    public void sendSMS(String to, String token) {
        TwilioRestClient client = new TwilioRestClient(SID, authToken);
        Account account = client.getAccount();
        SmsFactory smsFactory = account.getSmsFactory();

        Map<String, String> message = new HashMap<>();

        message.put("To", to);
        message.put("From", phoneNo);
        message.put("Body", messageBody.replace("{code}", token));

        try {
            smsFactory.create(message);
        } catch (TwilioRestException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }


    }
}
