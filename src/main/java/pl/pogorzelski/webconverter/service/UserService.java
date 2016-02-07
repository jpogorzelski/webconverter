package pl.pogorzelski.webconverter.service;

import pl.pogorzelski.webconverter.domain.User;
import pl.pogorzelski.webconverter.domain.dto.RegisterForm;
import pl.pogorzelski.webconverter.domain.dto.UserCreateForm;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(RegisterForm form);

    User create(UserCreateForm form);

    void incrementCurrentConversionCount(User user);
}
