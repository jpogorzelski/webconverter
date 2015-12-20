package pl.pogorzelski.webconverter.service.file;

import pl.pogorzelski.webconverter.domain.File;
import pl.pogorzelski.webconverter.domain.dto.ConvertForm;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by kuba on 11/23/15.
 */
public interface FileService {

    Optional<File> getFileById(long id);

    Optional<File> getFileByName(String name);

    Collection<File> getAllFiles();

    File create(ConvertForm form);
}
