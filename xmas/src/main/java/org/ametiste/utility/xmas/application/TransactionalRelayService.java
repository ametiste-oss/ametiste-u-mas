package org.ametiste.utility.xmas.application;

import org.ametiste.utility.xmas.application.weaver.RelayWeaver;
import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.infrastructure.FatalTransactionException;
import org.ametiste.utility.xmas.infrastructure.RelayCommand;
import org.ametiste.utility.xmas.infrastructure.RelayConfiguration;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by Daria on 24.12.2014.
 */
public class TransactionalRelayService implements RelayService {

    private final ExecutorService executor;
    private long lastUpdateCheck;
    private RelayConfigurationFactory relayConfigFactory;
    private List<RelayWeaver> weavers;
    private List<RelayConfiguration> configurations;

    public TransactionalRelayService(ExecutorService executor, RelayConfigurationFactory relayConfigFactory, List<RelayWeaver> weavers) {

        this.executor = executor;
        this.relayConfigFactory = relayConfigFactory;
        this.weavers = weavers;
        this.configurations = relayConfigFactory.loadConfigurations();
        applyWeaving();
    }

    @Override
    public void process(RawDataBox data) {


        configurations.stream().filter(configuration -> configuration.canTransfer(data)).forEach(configuration -> {
            if (configuration.isTransactional()) {
                processTransaction(configuration, data);
            } else {
                processNoTransaction(configuration, data);
            }
        });

    }

    private void processTransaction(RelayConfiguration configuration, RawDataBox data) {
        try {
            configuration.transfer(data);
        }
        catch (FatalTransactionException e) {
            throw e;
        }
    }

    private void processNoTransaction(RelayConfiguration configuration, RawDataBox data) {
        executor.execute(new RelayCommand(configuration, data));
    }

    private void applyWeaving() {
        for(RelayWeaver weaver: weavers) {
            configurations.forEach(weaver::wire);
        }
    }
}
