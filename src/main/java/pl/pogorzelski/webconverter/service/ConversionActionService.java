package pl.pogorzelski.webconverter.service;

import pl.pogorzelski.webconverter.convert.BaseConverter;
import pl.pogorzelski.webconverter.domain.User;

import java.io.File;

/**
 * @author Kuba
 */
public interface ConversionActionService {

    void convert(BaseConverter converter, File source, File target, User user);
}
