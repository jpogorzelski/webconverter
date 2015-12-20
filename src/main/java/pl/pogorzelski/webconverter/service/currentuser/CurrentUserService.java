package pl.pogorzelski.webconverter.service.currentuser;

import pl.pogorzelski.webconverter.domain.dto.CurrentUser;

public interface CurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}
