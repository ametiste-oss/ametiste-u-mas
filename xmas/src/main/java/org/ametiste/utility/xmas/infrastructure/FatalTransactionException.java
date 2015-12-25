package org.ametiste.utility.xmas.infrastructure;

import org.ametiste.utility.xmas.infrastructure.transaction.TransactionException;

/**
 * Created by Daria on 27.04.2015.
 */
public class FatalTransactionException extends RuntimeException {
    public FatalTransactionException(TransactionException e) {
        super(e);
    }
}
