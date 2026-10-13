package ru.golyakov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.golyakov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.golyakov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.golyakov.MySecondTestAppSpringBoot.model.*;
import ru.golyakov.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.golyakov.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.golyakov.MySecondTestAppSpringBoot.service.ValidationService;
import ru.golyakov.MySecondTestAppSpringBoot.util.DateTimeUnit;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifyRequestService;
    private final ModifyRequestService sourceModifier;
    private final ModifyRequestService timeModifier;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService")
                        ModifyResponseService modifyResponseService,
                        @Qualifier("ModifySystemNameRequestService")
                        ModifyRequestService modifyRequestService,
                        @Qualifier("ModifySourceRequestService")
                        ModifyRequestService sourceModifier,
                        @Qualifier("ModifySystemTimeRequestService")
                        ModifyRequestService timeModifier) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifyRequestService = modifyRequestService;
        this.sourceModifier = sourceModifier;
        this.timeModifier = timeModifier;
    }

    @PostMapping("/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {
        timeModifier.modify(request);
        log.info("Request-объект создан из тела запроса");
        log.info("request: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUnit.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
        log.info("Создан первоначальный Response с помощью билдера: {}", response);

        try {
            validationService.isValid(bindingResult);
            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException("uin 123 detected");
            }
        } catch (ValidationFailedException e) {
            log.error("Произошла ошибка валидации: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("По причине ошибки в Response изменены значения code, errorCode, errorMessage");
            log.info("Отправляеется ответ с кодом 400: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            log.error("Произошла ошибка uin=123");
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UIN123_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UIN123);
            log.info("По причине ошибки в Response изменены значения code, errorCode, errorMessage");
            log.info("Отправляеется ответ с кодом 400: {}", response);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Произошла непредвиденная ошибка: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("По причине ошибки в Response изменены значения code, errorCode, errorMessage");
            log.info("Отправляеется ответ с кодом 500: {}", response);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyResponseService.modify(response);
        log.info("К ответу применена модификация и изменено поле SystemTime");
        sourceModifier.modify(request);
        modifyRequestService.modify(request);
        log.info("Отправляется успешный ответ с кодом 200: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
