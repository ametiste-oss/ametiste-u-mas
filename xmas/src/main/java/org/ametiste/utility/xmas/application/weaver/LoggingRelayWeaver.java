package org.ametiste.utility.xmas.application.weaver;

import org.ametiste.utility.xmas.infrastructure.RelayConfiguration;
import org.ametiste.utility.xmas.infrastructure.logging.UmasLogger;

/**
 * Created by Daria on 24.04.2015.
 */
public class LoggingRelayWeaver implements RelayWeaver {

    private UmasLogger logger;

    public LoggingRelayWeaver(UmasLogger logger) {

        this.logger = logger;
    }

    @Override
    public void wire(RelayConfiguration configuration) {
        if(configuration instanceof LoggingWire) {
            ((LoggingWire)configuration).wireLogger(logger);
        }

    }
}
