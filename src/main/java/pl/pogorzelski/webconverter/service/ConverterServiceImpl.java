package pl.pogorzelski.webconverter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.pogorzelski.webconverter.domain.Converter;
import pl.pogorzelski.webconverter.domain.dto.NewConverterForm;
import pl.pogorzelski.webconverter.repository.ConverterRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConverterServiceImpl implements ConverterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterServiceImpl.class);


    private final ConverterRepository converterRepository;

    @Inject
    public ConverterServiceImpl(ConverterRepository converterRepository) {
        this.converterRepository = converterRepository;


        Converter format = new Converter("pdf", "jpg");
        converterRepository.save(format);

        Converter format1 = new Converter("pdf", "png");
        converterRepository.save(format1);

        Converter format2 = new Converter("pdf", "gif");
        converterRepository.save(format2);

        Converter format3 = new Converter("doc", "pdf");
        converterRepository.save(format3);

        Converter format4 = new Converter("doc", "jpg");
        converterRepository.save(format4);
    }

    @Override
    public Collection<String> getAllConverters() {
        LOGGER.debug("getting all formats");
        List<Converter> formats = converterRepository.findAll();
        Set<String> result = formats.stream().map(Converter::getSourceFormat).collect(Collectors.toSet());

        return result;
    }

    @Override
    public Collection<String> getBySourceFormat(String source) {
        LOGGER.debug("fetching destination formats for {}", source);
        List<Converter> format = converterRepository.findBySourceFormat(source);
        Set<String> result = format.stream().map(Converter::getTargetFormat).collect(Collectors.toSet());

        return result;
    }

    @Override
    public Optional<Converter> getOneBySourceFormatAndTargetFormat(String sourceFormat, String targetFormat) {
        return converterRepository.findOneBySourceFormatAndTargetFormat(sourceFormat, targetFormat);
    }


    @Override
    public Converter create(Converter converter) {
        return converterRepository.save(converter);
    }

    @Override
    public Converter create(NewConverterForm form) {
        Converter converter = new Converter();
        converter.setSourceFormat(form.getSourceFormat());
        converter.setTargetFormat(form.getTargetFormat());
        converter.setSourceCode(form.getSourceCode());
        return converterRepository.save(converter);

    }


}
