package com.banking.client.adapter;

import com.banking.client.port.AccountsClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestAccountsClient implements AccountsClient {

    private final RestTemplate restTemplate;

    @Value("${accounts.base-url:http://localhost:8081}")
    private String baseUrl;

    @Override
    public boolean hasAccounts(Long clientId) {
        String url = baseUrl + "/accounts/" + clientId;
        ResponseEntity<List> resp = restTemplate.getForEntity(url, List.class);
        List body = resp.getBody();
        return resp.getStatusCode().is2xxSuccessful() && body != null && !body.isEmpty();
    }
}
