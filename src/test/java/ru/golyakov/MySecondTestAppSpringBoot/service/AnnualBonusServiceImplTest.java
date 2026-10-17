package ru.golyakov.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import ru.golyakov.MySecondTestAppSpringBoot.model.Positions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnnualBonusServiceImplTest {

    @Test
    void calculate() {

        //given
        Positions position = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;
        int year = 2026;
        int yearVisocosny = 2024;


        //when
        double result = new AnnualBonusServiceImpl().calculate(position, salary, bonus, workDays, year);
        double resultVisocosny = new AnnualBonusServiceImpl().calculate(position, salary, bonus, workDays, yearVisocosny);

        //then
        assertAll(
                () -> assertEquals(159780.82, result, 0.01),
                () -> assertEquals(159344.26, resultVisocosny, 0.01)
        );
    }
}