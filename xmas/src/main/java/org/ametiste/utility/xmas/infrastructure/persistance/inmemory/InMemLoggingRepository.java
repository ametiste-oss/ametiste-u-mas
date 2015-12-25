package org.ametiste.utility.xmas.infrastructure.persistance.inmemory;

import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.domain.model.RelayResult;
import org.ametiste.utility.xmas.infrastructure.persistance.LoggingRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daria on 23.04.2015.
 */
public class InMemLoggingRepository implements LoggingRepository {

    private Map<Long, RawDataBox> map;
    private Map<Long, RelayResult> relayMap;

    public InMemLoggingRepository() {
        map = new HashMap<>();
        relayMap = new HashMap<>();
    }


    @Override
    public void saveOriginalMessage(RawDataBox data) {
        map.put(System.currentTimeMillis(), data);
    }

    @Override
    public void saveTransferResult(RelayResult result) {
        relayMap.put(System.currentTimeMillis(), result);
    }
}
