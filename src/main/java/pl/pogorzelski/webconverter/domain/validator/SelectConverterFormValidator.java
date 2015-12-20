package pl.pogorzelski.webconverter.domain.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.pogorzelski.webconverter.domain.dto.SelectConverterForm;

@Component
public class SelectConverterFormValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectConverterFormValidator.class);
//    private final ConverterService converterService;
//
//    @Inject
//    public SelectConverterFormValidator(ConverterService converterService) {
//        this.converterService = converterService;
//    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(SelectConverterForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("Validating {}", target);
        SelectConverterForm form = (SelectConverterForm) target;
        //  validateFileSize(errors, form);

    }

    private void validateFileSize(Errors errors, SelectConverterForm form) {
        /*if (!form.getPassword().equals(form.getPasswordRepeated())) {
            errors.reject("password.no_match", "Passwords do not match");
        }*/
    }
}


