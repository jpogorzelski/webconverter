package pl.pogorzelski.webconverter.service;

import pl.pogorzelski.webconverter.domain.dto.CurrentUser;

public interface CurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}
