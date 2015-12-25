package org.ametiste.utility.xmas.infrastructure.converter;

import java.util.Map;

/**
 * Created by Daria on 24.12.2014.
 */
public interface MessageConverter<T> {

	boolean isDataSupported(Map<String, Object> data);

	T convert(Map<String, Object> data);
}
