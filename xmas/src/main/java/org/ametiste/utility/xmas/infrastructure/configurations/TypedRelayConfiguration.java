package org.ametiste.utility.xmas.infrastructure.configurations;

import org.ametiste.utility.xmas.domain.model.Configuration;
import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.infrastructure.converter.MessageConverter;
import org.ametiste.utility.xmas.infrastructure.transaction.TransactionStrategy;

/**
 * Created by Daria on 24.12.2014.
 */
public class TypedRelayConfiguration<T>  extends  AbstractRelayConfiguration<T> {

    public TypedRelayConfiguration(String name, MessageConverter<T> converter, TransactionStrategy<T> strategy, Configuration ... configurations) {
        super(name,converter, strategy, configurations);
    }

    @Override
    public boolean canTransfer(RawDataBox data) {
		return converter.isDataSupported(data.getAsMap());
    }
}
