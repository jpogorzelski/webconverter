package pl.pogorzelski.webconverter.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pogorzelski.webconverter.domain.FileEntry;
import pl.pogorzelski.webconverter.repository.FileRepository;
import pl.pogorzelski.webconverter.service.FileService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Created by kuba on 11/23/15.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private FileRepository fileRepository;

    @Override
    public Optional<FileEntry> getFileById(long id) {
        return Optional.ofNullable(fileRepository.findOne(id));
    }

    @Override
    public Optional<FileEntry> getFileByName(String name) {
        return fileRepository.findOneByName(name);
    }

    @Override
    public Optional<FileEntry> getFileByMd5Hash(String md5Hash) {
        return fileRepository.findOneByMd5Hash(md5Hash);
    }

    @Override
    public List<FileEntry> getAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public void save(FileEntry file) {
        fileRepository.save(file);
    }

}
