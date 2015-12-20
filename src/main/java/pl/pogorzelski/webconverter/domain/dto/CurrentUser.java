package pl.pogorzelski.webconverter.domain.dto;

import org.springframework.security.core.authority.AuthorityUtils;
import pl.pogorzelski.webconverter.domain.Role;
import pl.pogorzelski.webconverter.domain.User;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = -1328148319292908962L;
    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }

    @Override
    public String toString() {
        return "CurrentUser{" + "user=" + user + "} " + super.toString();
    }
}
