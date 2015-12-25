package org.ametiste.utility.xmas.infrastructure.transaction;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class FormUrlencodedTransactionStrategy implements TransactionStrategy<Map<String, Object>> {


    private final RestTemplate template;
    private final String uri;

    public FormUrlencodedTransactionStrategy(RestTemplate template, String uri) {

        this.template = template;
        this.uri = uri;
    }

    @Override
    public void process(Map<String, Object> data) throws TransactionException {
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();

        for(Map.Entry<String, Object> entry: data.entrySet()) {
            bodyMap.add(entry.getKey(), entry.getValue().toString());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyMap, headers);
           template.exchange(uri, HttpMethod.POST, request, Void.class);
    }

    @Override
    public void processFailure(Map<String, Object> data) {
        //do nothing atm
    }
}
