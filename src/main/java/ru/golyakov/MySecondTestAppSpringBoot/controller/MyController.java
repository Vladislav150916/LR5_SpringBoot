
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
import ru.golyakov.MySecondTestAppSpringBoot.service.AnnualBonusService;
import ru.golyakov.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.golyakov.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.golyakov.MySecondTestAppSpringBoot.service.ValidationService;
import ru.golyakov.MySecondTestAppSpringBoot.util.DateTimeUnit;

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifyRequestService;
    private final ModifyRequestService sourceModifier;
    private final ModifyRequestService timeModifier;
    private final AnnualBonusService annualBonusService;

    @Autowired
    public MyController(ValidationService validationService,
            @Qualifier("ModifySystemTimeResponseService")
            ModifyResponseService modifyResponseService,
            @Qualifier("ModifySystemNameRequestService")
            ModifyRequestService modifyRequestService,
            @Qualifier("ModifySourceRequestService")
            ModifyRequestService sourceModifier,
            @Qualifier("ModifySystemTimeRequestService")
            ModifyRequestService timeModifier,
            AnnualBonusService annualBonusService) {

        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifyRequestService = modifyRequestService;
        this.sourceModifier = sourceModifier;
        this.timeModifier = timeModifier;
        this.annualBonusService = annualBonusService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<Response> feedback(
            @Valid @RequestBody Request request,
            BindingResult bindingResult) {

        try {
            timeModifier.modify(request);
            validationService.isValid(bindingResult);

            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException("uin 123 detected");
            }

            double annualBonus = annualBonusService.calculate(
                    request.getPosition(),
                    request.getSalary(),
                    request.getBonus(),
                    request.getWorkDays(),
                    request.getYear()
            );

            Response response = Response.builder()
                    .uid(request.getUid())
                    .operationUid(request.getOperationUid())
                    .systemTime(DateTimeUnit.getCustomFormat().format(new Date()))
                    .code(Codes.SUCCESS)
                    .errorCode(ErrorCodes.EMPTY)
                    .errorMessage(ErrorMessages.EMPTY)
                    .annualBonus(annualBonus)
                    .build();

            modifyResponseService.modify(response);
            sourceModifier.modify(request);
            modifyRequestService.modify(request);
            log.info("Запрос {} успешно обработан", request.getUid());
            return ResponseEntity.ok(response);

        } catch (ValidationFailedException e) {
            log.warn("Ошибка валидации: {}", e.getMessage());
            return errorResponse(
                    request,
                    ErrorCodes.VALIDATION_EXCEPTION,
                    ErrorMessages.VALIDATION,
                    HttpStatus.BAD_REQUEST
            );
        } catch (UnsupportedCodeException e) {
            log.warn("Отклонён запрос с UID 123");
            return errorResponse(
                    request,
                    ErrorCodes.UIN123_EXCEPTION,
                    ErrorMessages.UIN123,
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            log.error("Непредвиденная ошибка обработки запроса", e);
            return errorResponse(
                    request,
                    ErrorCodes.UNKNOWN_EXCEPTION,
                    ErrorMessages.UNKNOWN,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Формирует HTTP-ответ при ошибке обработки запроса.
     */
    private ResponseEntity<Response> errorResponse(
            Request request,
            ErrorCodes errorCode,
            ErrorMessages errorMessage,
            HttpStatus status) {

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUnit.getCustomFormat().format(new Date()))
                .code(Codes.FAILED)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();

        return new ResponseEntity<>(response, status);
    }
}
