package org.ametiste.utility.xmas.domain.model;

import java.util.UUID;

/**
 * Created by Daria on 24.04.2015.
 */
public class RelayResult {
    private final UUID id;
    private final String relayName;
    private final Object data;
    private final RelayResultStatus status;

    public RelayResult(UUID id, String relayName, Object data, RelayResultStatus status) {

        this.id = id;
        this.relayName = relayName;
        this.data = data;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getRelayName() {
        return relayName;
    }

    public Object getData() {
        return data;
    }

    public RelayResultStatus getStatus() {
        return status;
    }
}
