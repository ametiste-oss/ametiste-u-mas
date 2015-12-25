package org.ametiste.utility.xmas.infrastructure.logging;

import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.domain.model.RelayResult;
import org.ametiste.utility.xmas.infrastructure.persistance.LoggingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Daria on 28.04.2015.
 */
public class ByPropertyUmasLogger implements UmasLogger {

    private List<LoggingRepository> enabledLoggers;

    public ByPropertyUmasLogger(String logProperty, Map<String,LoggingRepository> loggers) {

        enabledLoggers = new ArrayList<>();

        for(Map.Entry<String, LoggingRepository> logger: loggers.entrySet()) {
            if(logger.getKey().equals(logProperty)) {
                enabledLoggers.add(logger.getValue());
            }
        }
    }

    @Override
    public void logOriginal(RawDataBox data) {
        for(LoggingRepository repo: enabledLoggers) {
            repo.saveOriginalMessage(data);
        }
    }

    @Override
    public void logTransferResult(RelayResult result) {
        for(LoggingRepository repo: enabledLoggers) {
            repo.saveTransferResult(result);
        }
    }
}
