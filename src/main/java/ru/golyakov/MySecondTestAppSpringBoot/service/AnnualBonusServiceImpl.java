package ru.golyakov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.golyakov.MySecondTestAppSpringBoot.model.Positions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService{

    @Override
    public double calculate(Positions position, double salary, double bonus, int workDays, int year) {

        int daysInYear = Year.of(year).length();
        double fullAnnualBonus = salary * bonus * position.getPositionCoefficient();
        double result = fullAnnualBonus * workDays / daysInYear;

        return BigDecimal.valueOf(result)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
