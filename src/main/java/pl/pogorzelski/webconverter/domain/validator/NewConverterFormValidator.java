package pl.pogorzelski.webconverter.domain.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.pogorzelski.webconverter.domain.Converter;
import pl.pogorzelski.webconverter.domain.dto.NewConverterForm;
import pl.pogorzelski.webconverter.service.ConverterService;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by kuba on 12/15/15.
 */
@Component
public class NewConverterFormValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewConverterFormValidator.class);

    @Inject
    ConverterService converterService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(NewConverterForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("Validating {}", target);
        NewConverterForm form = (NewConverterForm) target;
        validateTest(errors, form);
    }

    private void validateTest(Errors errors, NewConverterForm form) {
        Optional<Converter> converter = converterService.getOneBySourceFormatAndTargetFormat(form.getSourceFormat(), form.getTargetFormat());
        if (converter.isPresent()){
            errors.reject("error.converter.exists", "Taki konwerter już istnieje!");
        }
    }

}
