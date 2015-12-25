package org.ametiste.utility.xmas.infrastructure.transaction;

/**
 * Created by Daria on 24.12.2014.
 */
public interface TransactionStrategy<T> {

	/**
	 * 
	 * processes data with defined way. TransactionException is marker for service using TransactionStrategy to call
	 * processFailure if required. No other exceptions are catched during service work
	 * 
	 * @param data
	 * @throws TransactionException
	 */
	void process(T data) throws TransactionException;

	void processFailure(T data);
}
