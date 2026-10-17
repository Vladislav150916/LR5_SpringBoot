package ru.golyakov.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.golyakov.MySecondTestAppSpringBoot.model.Request;
import ru.golyakov.MySecondTestAppSpringBoot.model.Systems;

@Slf4j
@Service
@Qualifier("ModifySystemNameRequestService")
public class ModifySystemNameRequestService  implements ModifyRequestService{
    @Override
    public void modify(Request request) {
        request.setSystemName(Systems.SERVICE_1);

        HttpEntity<Request> httpEntity = new HttpEntity<>(request);

        try {
            new RestTemplate().exchange("http://localhost:8084/feedback",
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<Object>() {
                    });
        } catch (RestClientException e) {
            log.warn("Передача запроса сервису 2 не выполнена, проверьте, что Сервис 2 включен");
        }
    }
}
