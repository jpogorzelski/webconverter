package pl.pogorzelski.webconverter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pogorzelski.webconverter.domain.FileEntry;

import java.util.Optional;

/**
 * Created by kuba on 11/23/15.
 */
@Repository
public interface FileRepository extends JpaRepository<FileEntry, Long> {

    Optional<FileEntry> findOneByName(String name);

    Optional<FileEntry> findOneByMd5Hash(String md5Hash);
}