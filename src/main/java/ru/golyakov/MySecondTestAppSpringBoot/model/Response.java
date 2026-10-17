
package ru.golyakov.MySecondTestAppSpringBoot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    /**
     * Уникальный идентификатор запроса.
     */
    private String uid;

    /**
     * Уникальный идентификатор операции.
     */
    private String operationUid;

    /**
     * Дата и время формирования ответа.
     */
    private String systemTime;

    /**
     * Код результата обработки запроса.
     */
    private Codes code;

    /**
     * Код ошибки, возникшей при обработке запроса.
     */
    private ErrorCodes errorCode;

    /**
     * Текстовое описание ошибки.
     */
    private ErrorMessages errorMessage;

    /**
     * Рассчитанная годовая премия сотрудника.
     */
    private Double annualBonus;
}
