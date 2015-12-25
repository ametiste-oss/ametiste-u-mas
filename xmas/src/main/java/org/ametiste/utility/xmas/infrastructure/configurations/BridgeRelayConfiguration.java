package org.ametiste.utility.xmas.infrastructure.configurations;

import org.ametiste.utility.xmas.domain.model.Configuration;

import org.ametiste.utility.xmas.infrastructure.converter.NoConverter;
import org.ametiste.utility.xmas.infrastructure.transaction.ObjectTransactionStrategy;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Daria on 24.12.2014.
 */
public class BridgeRelayConfiguration extends TypedRelayConfiguration<Object> {


    private static final String name = "BridgeRelayConfiguration";

    public BridgeRelayConfiguration(RestTemplate template, String uri, Configuration... configurations) {
        super(name, new NoConverter(), new ObjectTransactionStrategy(template, uri), configurations);
    }
}
