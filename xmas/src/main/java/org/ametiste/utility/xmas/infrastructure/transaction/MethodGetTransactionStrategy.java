package org.ametiste.utility.xmas.infrastructure.transaction;

import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class MethodGetTransactionStrategy implements TransactionStrategy<Map<String, Object>> {


    private final RestTemplate template;
    private final String uri;

    public MethodGetTransactionStrategy(RestTemplate template, String uri) {

        this.template = template;
        this.uri = uri;
    }

    @Override
    public void process(Map<String, Object> data) throws TransactionException {
        StringBuilder b = new StringBuilder("?");

        for(Map.Entry<String, Object> entry: data.entrySet()) {
           b.append(entry.getKey()).append("=").append(entry.getValue().toString()).append("&");
        }
        template.getForEntity(uri + b.toString(), Void.class);
    }

    @Override
    public void processFailure(Map<String, Object> data) {
        //do nothing atm
    }
}
