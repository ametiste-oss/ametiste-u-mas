package org.ametiste.utility.xmas.infrastructure.converter;

import java.util.Map;

/**
 * Created by Daria on 24.12.2014.
 */
public class NoConverter<T> implements MessageConverter<T> {

	@Override
	public boolean isDataSupported(Map<String, Object> data) {
		return true;
	}

	@Override
	public T convert(Map<String, Object> data) {
		return (T) data;
	}
}
