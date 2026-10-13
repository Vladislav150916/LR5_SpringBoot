package ru.golyakov.MySecondTestAppSpringBoot.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.golyakov.MySecondTestAppSpringBoot.model.Request;
import ru.golyakov.MySecondTestAppSpringBoot.util.DateTimeUnit;

import java.util.Date;

@Service
@Qualifier("ModifySystemTimeRequestService")
public class ModifySystemTimeRequestService implements ModifyRequestService{
    @Override
    public void modify(Request request) {
        request.setSystemTime(DateTimeUnit.getCustomFormat().format(new Date()));
    }
}
