package org.ametiste.utility.xmas.infrastructure.persistance;

import org.ametiste.utility.xmas.domain.model.RawDataBox;
import org.ametiste.utility.xmas.domain.model.RelayResult;
import org.ametiste.utility.xmas.infrastructure.persistance.inmemory.InMemLoggingRepository;

/**
 * Created by Daria on 23.04.2015.
 */
public class TestLoggingRepository implements LoggingRepository {

    private InMemLoggingRepository repo = new InMemLoggingRepository();
    private int originals = 0;
    private int results = 0;

    @Override
    public void saveOriginalMessage(RawDataBox data) {
        if(data.containsKey("error")) {
            throw new IllegalArgumentException();
        }
        repo.saveOriginalMessage(data);
        originals ++;
    }

    @Override
    public void saveTransferResult(RelayResult result) {
        repo.saveTransferResult(result);
        results ++;
    }

    public void assertOriginalWasLogged(int times) {
        if(times!=originals) {
            throw new AssertionError("Original message was expected to be logged " + times +  " times, but was called " + originals + " times");
        }
    }

    public void assertTransferResultWasLogged(int times) {
        if(times!=results) {
            throw new AssertionError("Transfer result was expected to be logged " + times +  " times, but was called " + results + " times");
        }
    }

    public void reset() {
        originals = 0;
        results = 0;
    }
}
