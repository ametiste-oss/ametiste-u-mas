package org.ametiste.utility.xmas.infrastructure.transaction;

import org.springframework.web.client.RestTemplate;

/**
 * Created by Daria on 24.12.2014.
 */
public class ObjectTransactionStrategy extends  ParametrizedTransactionStrategy<Object> {

    public ObjectTransactionStrategy(RestTemplate template, String uri) {
		super(template, uri);
    }
}
