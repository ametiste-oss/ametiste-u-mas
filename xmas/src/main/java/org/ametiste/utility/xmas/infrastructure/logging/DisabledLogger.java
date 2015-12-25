package org.ametiste.utility.xmas.infrastructure.logging;

import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.domain.model.RelayResult;

/**
 * Created by Daria on 28.04.2015.
 */
public class DisabledLogger implements UmasLogger {

    public DisabledLogger() {

    }

    @Override
    public void logOriginal(RawDataBox data) {

    }

    @Override
    public void logTransferResult(RelayResult result) {

    }
}
