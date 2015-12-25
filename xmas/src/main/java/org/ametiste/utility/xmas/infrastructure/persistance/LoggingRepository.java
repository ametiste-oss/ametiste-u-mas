package org.ametiste.utility.xmas.infrastructure.persistance;

import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.domain.model.RelayResult;

/**
 * Created by Daria on 23.04.2015.
 */
public interface LoggingRepository {

    void saveOriginalMessage(RawDataBox data);

    void saveTransferResult(RelayResult result);
}
