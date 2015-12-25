package org.ametiste.utility.xmas.infrastructure;

import org.ametiste.utility.xmas.domain.model.RawDataBox;

/**
 * Created by Daria on 24.12.2014.
 */
public class RelayCommand implements Runnable {

    private final RelayConfiguration configuration;
    private final RawDataBox data;

    public RelayCommand(RelayConfiguration configuration, RawDataBox data) {

        this.configuration = configuration;
        this.data = data;
    }

    @Override
    public void run() {
        configuration.transfer(data);
    }
}
