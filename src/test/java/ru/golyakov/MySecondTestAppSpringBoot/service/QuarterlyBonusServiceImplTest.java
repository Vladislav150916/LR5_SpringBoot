package ru.golyakov.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.golyakov.MySecondTestAppSpringBoot.model.Positions;

import static org.junit.jupiter.api.Assertions.*;

class QuarterlyBonusServiceImplTest {

    @Test
    void calculate() {
        //given
        QuarterlyBonusServiceImpl service = new QuarterlyBonusServiceImpl();
        Positions manager = Positions.PO;
        Positions noManager = Positions.DEV;
        double salary = 100000.00;
        double bonus = 2.0;

        //when
        double resultManager = service.calculate(manager, salary, bonus);
        Executable resultForNoManager = () -> service.calculate(noManager, salary, bonus);

        //then
        assertAll(
                //Проверка менеджера
                () -> assertEquals(200000.00, resultManager, 0.001),
                //Проверка не менеджера
                () -> assertThrows(IllegalArgumentException.class, resultForNoManager)
        );
    }
}