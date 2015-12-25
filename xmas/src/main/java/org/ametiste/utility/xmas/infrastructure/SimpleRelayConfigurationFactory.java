package org.ametiste.utility.xmas.infrastructure;

import java.util.List;

/**
 * Created by Daria on 28.01.2015.
 */
public class SimpleRelayConfigurationFactory extends AbstractRelayConfigurationFactory {


    private List<RelayConfiguration> configurations;

    public void setConfigurationList(List<RelayConfiguration> configurations) {

        this.configurations = configurations;

    }

    @Override
    public List<RelayConfiguration> loadConfigurations() {
        return configurations;
    }

    @Override
    public boolean configurationsChangedSince(long timestamp) {
        return false;
    }
}
