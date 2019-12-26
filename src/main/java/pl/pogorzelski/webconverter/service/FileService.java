package pl.pogorzelski.webconverter.service;

import pl.pogorzelski.webconverter.domain.FileEntry;

import java.util.List;
import java.util.Optional;

/**
 * Created by kuba on 11/23/15.
 */
public interface FileService {

    Optional<FileEntry> getFileById(long id);

    Optional<FileEntry> getFileByName(String name);

    Optional<FileEntry> getFileByMd5Hash(String name);

    List<FileEntry> getAllFiles();

    void save(FileEntry file);


}
