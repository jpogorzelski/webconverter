package pl.pogorzelski.webconverter.domain.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.pogorzelski.webconverter.domain.dto.NewConverterForm;

/**
 * Created by kuba on 12/15/15.
 */
@Component
public class NewConverterFormValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewConverterFormValidator.class);

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
        if (form.getSourceFormat().trim().equals("pif")) {
            errors.rejectValue("sourceFormat","wrong.source.format", "Nieprawidlowy format zrodlowy");
        }
    }

}
