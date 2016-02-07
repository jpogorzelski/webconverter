package pl.pogorzelski.webconverter.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pogorzelski.webconverter.domain.User;
import pl.pogorzelski.webconverter.domain.dto.CurrentUser;

/**
 * @author Kuba
 */
@Service
public class SecurityUtils {

    public User getLoggedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        CurrentUser principal = (CurrentUser) authentication.getPrincipal();
        return principal.getUser();
    }
}
