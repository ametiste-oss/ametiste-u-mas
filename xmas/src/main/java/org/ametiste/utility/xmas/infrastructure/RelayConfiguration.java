package org.ametiste.utility.xmas.infrastructure;

import org.ametiste.utility.xmas.domain.model.RawDataBox;

/**
 * Created by Daria on 24.12.2014.
 */
public interface RelayConfiguration {

    void transfer(RawDataBox data) throws FatalTransactionException;

    boolean canTransfer(RawDataBox data);

    boolean isTransactional();
}
