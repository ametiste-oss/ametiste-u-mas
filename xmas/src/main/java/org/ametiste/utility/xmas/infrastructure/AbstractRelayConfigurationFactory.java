package org.ametiste.utility.xmas.infrastructure;

import java.util.*;

import org.ametiste.utility.xmas.domain.model.Configuration;
import org.ametiste.utility.xmas.infrastructure.transaction.MethodGetTransactionStrategy;
import org.ametiste.utility.xmas.application.RelayConfigurationFactory;
import org.ametiste.utility.xmas.infrastructure.configurations.*;
import org.ametiste.utility.xmas.infrastructure.transaction.TransactionStrategy;
import org.springframework.web.client.RestTemplate;

import org.ametiste.utility.xmas.infrastructure.converter.MessageConverter;
import org.ametiste.utility.xmas.infrastructure.converter.NoConverter;
import org.ametiste.utility.xmas.infrastructure.transaction.FormUrlencodedTransactionStrategy;

/**
 * Created by Daria on 29.01.2015.
 */
public class AbstractRelayConfigurationFactory implements RelayConfigurationFactory {

    private final List<RelayConfiguration> configs;
    private final RestTemplate defaultRestTemplate = new RestTemplate();
    private long lastUpdated;

    public AbstractRelayConfigurationFactory() {

        lastUpdated = System.currentTimeMillis();
        configs = new ArrayList<>();
    }

    @Override
    public List<RelayConfiguration> loadConfigurations() {
        return Collections.unmodifiableList(configs);
    }

    @Override
    public boolean configurationsChangedSince(long timestamp) {
        return timestamp<lastUpdated;
    }

    protected void addBridgeConfiguration(String uri) {
        addConfig(new BridgeRelayConfiguration(defaultRestTemplate, uri));
    }

    protected void addBridgeConfiguration(String uri, Configuration... configurations) {
        addConfig(new BridgeRelayConfiguration(defaultRestTemplate, uri, configurations));
    }



    protected void addBridgeConfiguration(RestTemplate template, String uri) {
        addConfig(new BridgeRelayConfiguration(template, uri));
    }

    protected void addBridgeConfiguration(RestTemplate template, String uri, Configuration ... configurations) {
        addConfig(new BridgeRelayConfiguration(template, uri, configurations));
    }

	protected <T> void addTransformConfiguration(MessageConverter<T> converter, String uri) {
        addConfig(new TransformRelayConfiguration<T>(converter, defaultRestTemplate, uri));
    }

    protected <T> void addTransformConfiguration(MessageConverter<T> converter, String uri, Configuration ... configurations) {
        addConfig(new TransformRelayConfiguration<T>(converter, defaultRestTemplate, uri, configurations));
    }

	protected <T> void addTransformConfiguration(MessageConverter<T> converter, RestTemplate template, String uri, Configuration ... configurations) {
        addConfig(new TransformRelayConfiguration<T>(converter, template, uri, configurations));
    }

    protected <T> void addParametriziedConfiguration(MessageConverter<T> converter, TransactionStrategy<T> strategy) {
        addConfig(new TypedRelayConfiguration<T>("ParametriziedRelayConfiguration", converter, strategy));
    }

    protected <T> void addParametriziedConfiguration(MessageConverter<T> converter, TransactionStrategy<T> strategy, Configuration ... configurations) {
        addConfig(new TypedRelayConfiguration<T>("ParametriziedRelayConfiguration", converter, strategy, configurations));
    }

    protected void addCustomConfiguration(RelayConfiguration configuration) {
        addConfig(configuration);
    }

	protected void addFormUrlencodedDataBridge(String uri) {
        addConfig(new FormUriEncodedRelayConfiguration<>(new NoConverter(), new FormUrlencodedTransactionStrategy(
                defaultRestTemplate, uri)));
	}

    protected void addFormUrlencodedDataBridge(String uri, Configuration ... configurations) {
        addConfig(new FormUriEncodedRelayConfiguration<>(new NoConverter(), new FormUrlencodedTransactionStrategy(
                defaultRestTemplate, uri),configurations));
    }

    protected void addGETMethodDataBridge(String uri, Configuration ... configurations) {
        addConfig(new GetRelayConfiguration<>(new NoConverter(), new MethodGetTransactionStrategy(
                defaultRestTemplate, uri),configurations));
    }

    protected void addGETMethodDataConfiguration(String uri, MessageConverter<Map<String,Object>> converter, Configuration ... configurations) {
        addConfig(new GetRelayConfiguration<>(converter, new MethodGetTransactionStrategy(
                defaultRestTemplate, uri),configurations));
    }

    private void addConfig(RelayConfiguration config) {
        configs.add(config);
        this.lastUpdated = System.currentTimeMillis();
    }



}

