package ru.golyakov.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.golyakov.MySecondTestAppSpringBoot.exception.ValidationFailedException;

@Slf4j
@Service
public class RequestValidationService implements ValidationService{

    @Override
    public void isValid(BindingResult bindingResult) throws ValidationFailedException {
        if (bindingResult.hasErrors()) {
            log.error("В bindign Result обнаружены ошибки : {}", bindingResult.getAllErrors());
            throw new
                    ValidationFailedException(bindingResult.getFieldError().toString());
        }
        log.info("Валидация запроса успешно завершена");
    }
}
