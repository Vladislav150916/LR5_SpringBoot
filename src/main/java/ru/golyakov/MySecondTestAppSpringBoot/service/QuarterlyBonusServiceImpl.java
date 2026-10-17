package ru.golyakov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.golyakov.MySecondTestAppSpringBoot.model.Positions;

@Service
public class QuarterlyBonusServiceImpl implements QuarterlyBonusService {

    @Override
    public double calculate(Positions position, double salary, double bonus) {

        if (!position.isManager()) {
            throw new IllegalArgumentException("Сотрудник не является менеджером, квартальная премия не полагается");
        }

        return salary * bonus * position.getPositionCoefficient() / 4;
    }
}