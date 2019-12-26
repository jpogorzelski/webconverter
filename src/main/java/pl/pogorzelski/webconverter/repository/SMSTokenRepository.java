package pl.pogorzelski.webconverter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pogorzelski.webconverter.domain.SMSToken;
import pl.pogorzelski.webconverter.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Kuba
 */
public interface SMSTokenRepository extends JpaRepository<SMSToken, Long> {
    Optional<SMSToken> findByToken(String token);

    List<SMSToken> findAllByUser(User user);

    List<SMSToken> findAllByUserOrderByCreateDateAsc(User user);
    List<SMSToken> findAllByUserOrderByCreateDateDesc(User user);
}
