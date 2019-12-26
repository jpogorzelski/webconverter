package pl.pogorzelski.webconverter.domain;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Kuba
 */
@Entity
public class SMSToken {
    @Id
    @GeneratedValue
    private long id;
    private String token;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    private String phoneNo;

    private boolean expired=false;

    private LocalDateTime createDate;

    public SMSToken() { }

    public SMSToken(User user) {
      //  this.phoneNo = phoneNo;
        this.user = user;
        createDate = LocalDateTime.now();
        token = RandomStringUtils.randomAlphanumeric(5);
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isExpired() {
        final LocalDateTime minValid = LocalDateTime.now().minusMinutes(2);
        return expired || minValid.isAfter(getCreateDate());
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "SMSToken{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", phoneNo='" + phoneNo + '\'' +
                ", expired=" + expired +
                ", createDate=" + createDate +
                '}';
    }
}
