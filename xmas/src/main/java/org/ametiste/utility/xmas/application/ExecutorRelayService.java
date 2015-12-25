package org.ametiste.utility.xmas.application;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.ametiste.utility.xmas.application.weaver.RelayWeaver;
import org.ametiste.utility.xmas.domain.model.RawDataBox;

import org.ametiste.utility.xmas.infrastructure.RelayCommand;
import org.ametiste.utility.xmas.infrastructure.RelayConfiguration;

/**
 * Created by Daria on 24.12.2014.
 */
public class ExecutorRelayService implements RelayService {

    private final ExecutorService executor;
    private long lastUpdateCheck;
    private RelayConfigurationFactory relayConfigFactory;
    private List<RelayWeaver> weavers;
    private List<RelayConfiguration> configurations;

    public ExecutorRelayService(ExecutorService executor, RelayConfigurationFactory relayConfigFactory, List<RelayWeaver> weavers) {

        this.executor = executor;
        this.relayConfigFactory = relayConfigFactory;
        this.weavers = weavers;
        this.configurations = relayConfigFactory.loadConfigurations();
       // this.lastUpdateCheck = System.currentTimeMillis();
        applyWeaving();
    }

    @Override
    public void process(RawDataBox data) {


//        if(relayConfigFactory.configurationsChangedSince(lastUpdateCheck)) {
//            configurations = relayConfigFactory.loadConfigurations();
//            applyWeaving();
//            lastUpdateCheck = System.currentTimeMillis();
//        }

        configurations.stream().filter(configuration -> configuration.canTransfer(data)).forEach(configuration -> executor.execute(new RelayCommand(configuration, data)));


    }

    private void applyWeaving() {
        for(RelayWeaver weaver: weavers) {
            configurations.forEach(weaver::wire);
        }
    }
}
