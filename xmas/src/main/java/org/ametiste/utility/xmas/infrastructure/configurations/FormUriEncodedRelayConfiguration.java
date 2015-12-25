package org.ametiste.utility.xmas.infrastructure.configurations;

import org.ametiste.utility.xmas.domain.model.Configuration;
import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.infrastructure.converter.MessageConverter;
import org.ametiste.utility.xmas.infrastructure.transaction.TransactionStrategy;

/**
 * Created by Daria on 16.02.2015.
 */
public class FormUriEncodedRelayConfiguration<T> extends AbstractRelayConfiguration<T> {

    private static String name = "FormUriEncodedRelayConfiguration";

    public FormUriEncodedRelayConfiguration(MessageConverter<T> converter, TransactionStrategy<T> strategy, Configuration... configurations) {
        super(name,converter, strategy, configurations);
    }

    @Override
    public boolean canTransfer(RawDataBox data) {
        return data.getContentType().equals("application/x-www-form-urlencoded");

    }
}
