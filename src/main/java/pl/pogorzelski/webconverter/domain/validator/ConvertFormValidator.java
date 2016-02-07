package pl.pogorzelski.webconverter.domain.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.pogorzelski.webconverter.domain.dto.ConvertForm;
import pl.pogorzelski.webconverter.service.FileService;

import javax.inject.Inject;

@Component
public class ConvertFormValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertFormValidator.class);

    @Inject
    private FileService fileService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ConvertForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("Validating {}", target);
        ConvertForm form = (ConvertForm) target;
        validateEmail(errors, form);
    }

    private void validateEmail(Errors errors, ConvertForm form) {
        if (fileService.getFileByName(form.getName()).isPresent()) {
            errors.reject("email.exists", "User with this email already exists");
        }
    }


}
