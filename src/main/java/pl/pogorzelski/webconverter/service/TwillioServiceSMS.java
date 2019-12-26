package pl.pogorzelski.webconverter.service;

/**
 * @author Kuba
 */
public interface TwillioServiceSMS {

    void sendSMS(String to, String token);
}
