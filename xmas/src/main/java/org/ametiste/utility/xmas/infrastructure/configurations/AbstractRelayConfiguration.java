package org.ametiste.utility.xmas.infrastructure.configurations;

import org.ametiste.utility.xmas.application.weaver.LoggingWire;
import org.ametiste.utility.xmas.infrastructure.RelayConfiguration;
import org.ametiste.utility.xmas.infrastructure.converter.MessageConverter;
import org.ametiste.utility.xmas.infrastructure.FatalTransactionException;
import org.ametiste.utility.xmas.infrastructure.logging.UmasLogger;
import org.ametiste.utility.xmas.infrastructure.transaction.TransactionException;
import org.ametiste.utility.xmas.infrastructure.transaction.TransactionStrategy;
import org.ametiste.utility.xmas.domain.model.*;

import java.util.*;

/**
 * Created by Daria on 24.12.2014.
 */
public abstract class AbstractRelayConfiguration<T>  implements RelayConfiguration, LoggingWire {


    protected final MessageConverter<T> converter;
    private final TransactionStrategy<T> strategy;
	private String relayName;
	private UmasLogger umasLogger;
	private Set<Configuration> configs;
	private Configuration[] defaultConfiguration = new Configuration[]{RelayLogging.ENABLED, TransactionSupport.DISABLED};

	public AbstractRelayConfiguration(String relayName, MessageConverter<T> converter, TransactionStrategy<T> strategy, Configuration ... configs) {

		this.relayName = relayName;
		this.converter = converter;
        this.strategy = strategy;

		this.configs = new HashSet<>(combineConfigs(configs));
	}

	public AbstractRelayConfiguration(String relayName, MessageConverter<T> converter, TransactionStrategy<T> strategy) {

		this.relayName = relayName;
		this.converter = converter;
		this.strategy = strategy;

		this.configs = new HashSet<>(Arrays.asList(defaultConfiguration));
	}

    @Override
    public void transfer(RawDataBox data) throws FatalTransactionException {
		if (converter.isDataSupported(data.getAsMap())) {
			T typedData = converter.convert(data.getAsMap());


			try {
				strategy.process(typedData);
				log(data, typedData, RelayResultStatus.SUCCESS);

			} catch (TransactionException e) {
				strategy.processFailure(typedData);
				if(isTransactional()) {
					throw new FatalTransactionException(e);
				}

				//TODO hack that should be changed when process failure really will exist
				log(data, typedData, RelayResultStatus.FAILURE);
			}
		} else {
			throw new IllegalArgumentException("MessageConverter cant convert data: " + data.getAsMap());
		}
    }

	public boolean isTransactional() {
		return configs.contains(TransactionSupport.ENABLED);
	}

	public void wireLogger(UmasLogger umasLogger) {

		this.umasLogger = umasLogger;
	}

	private void log(RawDataBox data, T typedData, RelayResultStatus status) {
		if(configs.contains(RelayLogging.ENABLED)) {
			if(umasLogger == null) {
				throw new IllegalArgumentException("Configuration expects logging repository to be wired, but it wasnt");
			}
			umasLogger.logTransferResult(new RelayResult(data.getId(),relayName,typedData, status));
		}
	}

	private List<Configuration> combineConfigs(Configuration[] userConfigurations) {

		List<Configuration> configs = new ArrayList<>(Arrays.asList(userConfigurations));

		Map<Class<? extends Configuration>, Configuration> classes = new HashMap<>();
		for(Configuration userConfig: userConfigurations) {
			classes.put(userConfig.getClass(), userConfig);
		}

		for(Configuration defaultConfig : defaultConfiguration) {
			if(!classes.containsKey(defaultConfig.getClass())) {
				configs.add(defaultConfig);
			}
		}
		return configs;
	}

}
