package pl.pogorzelski.webconverter.service.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.pogorzelski.webconverter.domain.File;
import pl.pogorzelski.webconverter.domain.dto.ConvertForm;
import pl.pogorzelski.webconverter.repository.FileRepository;
import pl.pogorzelski.webconverter.service.user.UserServiceImpl;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;

/**
 * Created by kuba on 11/23/15.
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private FileRepository fileRepository;

    @Override
    public Optional<File> getFileById(long id) {
        return Optional.ofNullable(fileRepository.findOne(id));
    }

    @Override
    public Optional<File> getFileByName(String name) {
        return fileRepository.findOneByName(name);
    }

    @Override
    public Collection<File> getAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public File create(ConvertForm form) {
        File file = new File();
        file.setName(form.getName());
        return fileRepository.save(file);
    }
}
