package ru.golyakov.MySecondTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorMessages {

    EMPTY(""),
    VALIDATION("Ошибка валидации"),
    UIN123("Введен uin 123"),
    UNKNOWN("Не поддерживаемая ошибка");

    private final String description;


    ErrorMessages(String description) {
        this.description = description;
    }

    @JsonValue
    public String getName(){
        return description;
    }
}
