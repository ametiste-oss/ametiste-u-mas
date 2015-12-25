package org.ametiste.utility.xmas.infrastructure.configurations;

import org.ametiste.utility.xmas.domain.model.Configuration;
import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.infrastructure.converter.MessageConverter;
import org.ametiste.utility.xmas.infrastructure.transaction.TransactionStrategy;

/**
 * Created by ametiste on 8/11/15.
 */
public class GetRelayConfiguration<T> extends AbstractRelayConfiguration<T> {

    private static String name = "GETRelayConfiguration";

    public GetRelayConfiguration(MessageConverter<T> converter, TransactionStrategy<T> strategy, Configuration... configs) {
        super(name, converter, strategy, configs);
    }

    @Override
    public boolean canTransfer(RawDataBox data) {
        return true;
    }
}
