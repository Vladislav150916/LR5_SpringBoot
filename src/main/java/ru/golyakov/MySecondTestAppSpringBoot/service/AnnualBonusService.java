package ru.golyakov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.golyakov.MySecondTestAppSpringBoot.model.Positions;

@Service
public interface AnnualBonusService {

    double calculate(Positions position, double salary,
                     double bonus, int workDays, int year);
}
