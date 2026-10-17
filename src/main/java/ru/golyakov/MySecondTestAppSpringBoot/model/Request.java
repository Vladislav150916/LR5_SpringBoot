
package ru.golyakov.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    /**
     * Уникальный идентификатор запроса.
     */
    @NotBlank(message = "UID не может быть пустым")
    @Size(max = 32)
    private String uid;

    /**
     * Уникальный идентификатор операции.
     */
    @Size(max = 32)
    private String operationUid;

    /**
     * Наименование системы, отправившей запрос.
     */
    private Systems systemName;

    /**
     * Дата и время формирования запроса.
     */
    @NotBlank
    private String systemTime;

    /**
     * Источник поступления запроса.
     */
    private String source;

    /**
     * Должность сотрудника, для которого рассчитывается премия.
     */
    private Positions position;

    /**
     * Размер заработной платы сотрудника.
     */
    private Double salary;

    /**
     * Множитель для расчёта премии сотрудника.
     */
    private Double bonus;

    /**
     * Количество календарных дней, отработанных сотрудником за год.
     */
    private Integer workDays;

    /**
     * Год, за который производится расчёт премии.
     */
    private Integer year;

    /**
     * Идентификатор коммуникации.
     */
    @Min(1)
    @Max(100000)
    private int communicationId;

    /**
     * Идентификатор шаблона сообщения.
     */
    private int templateId;

    /**
     * Код продукта.
     */
    private int productCode;

    /**
     * Код SMS-сообщения.
     */
    private int smsCode;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }
}
