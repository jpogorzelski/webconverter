package pl.pogorzelski.webconverter.service;

import pl.pogorzelski.webconverter.domain.Converter;
import pl.pogorzelski.webconverter.domain.dto.NewConverterForm;

import java.util.Collection;
import java.util.Optional;

public interface ConverterService {

    Collection<String> getAllConverters();

    Collection<String> getBySourceFormat(String from);

    Optional<Converter> getOneBySourceFormatAndTargetFormat(String sourceFormat, String targetFormat);

    Converter create(Converter possibleFormats);

    Converter create(NewConverterForm form);


}
