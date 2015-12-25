package org.ametiste.utility.xmas.application;

import org.ametiste.utility.xmas.infrastructure.RelayConfiguration;

import java.util.List;

/**
 * Created by Daria on 28.01.2015.
 */
public interface RelayConfigurationFactory {

    List<RelayConfiguration> loadConfigurations();

    boolean configurationsChangedSince(long timestamp);

}
