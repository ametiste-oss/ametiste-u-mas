package org.ametiste.utility.xmas.infrastructure.configurations;

import org.ametiste.utility.xmas.domain.model.Configuration;
import org.springframework.web.client.RestTemplate;

import org.ametiste.utility.xmas.infrastructure.converter.MessageConverter;
import org.ametiste.utility.xmas.infrastructure.transaction.ParametrizedTransactionStrategy;

/**
 * Created by Daria on 29.01.2015.
 */
public class TransformRelayConfiguration<T> extends TypedRelayConfiguration<T> {

	private static final String name  ="TransformRelayConfiguration";

	public TransformRelayConfiguration(MessageConverter<T> converter, RestTemplate template, String uri, Configuration... configurations) {
		super(name, converter, new ParametrizedTransactionStrategy<T>(template, uri), configurations);
    }
}
