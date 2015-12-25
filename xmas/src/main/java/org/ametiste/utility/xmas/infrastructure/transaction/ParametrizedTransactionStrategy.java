package org.ametiste.utility.xmas.infrastructure.transaction;

import org.springframework.web.client.RestTemplate;

/**
 * Created by Daria on 29.01.2015.
 */
public class ParametrizedTransactionStrategy<T> implements TransactionStrategy<T> {

    private final RestTemplate restTemplate;
    private final String uri;

	public ParametrizedTransactionStrategy(RestTemplate template, String uri) {
        this.restTemplate = template;
        this.uri = uri;
    }

    @Override
    public void process(T typedData) {
		try {
			restTemplate.postForObject(uri, typedData, Object.class);
		} catch (Exception e) {
			throw new TransactionException(e);
		}
    }

    @Override
    public void processFailure(T typedData) {

    }
}
