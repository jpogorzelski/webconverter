package pl.pogorzelski.webconverter.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.pogorzelski.webconverter.domain.Role;
import pl.pogorzelski.webconverter.domain.dto.CurrentUser;
import pl.pogorzelski.webconverter.service.CurrentUserService;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetailsService.class);

    @Override
    public boolean canAccessUser(CurrentUser currentUser, Long userId) {
        LOGGER.debug("Checking if user={} has access to user={}", currentUser, userId);
        if (currentUser != null) {
            if ((currentUser.getRole() == Role.ADMIN) || currentUser.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

}
