package pl.pogorzelski.webconverter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pogorzelski.webconverter.domain.File;

import java.util.Optional;

/**
 * Created by kuba on 11/23/15.
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findOneByName(String name);
}