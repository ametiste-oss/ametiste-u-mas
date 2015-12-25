package org.ametiste.utility.xmas.infrastructure.converter;

import org.ametiste.utility.xmas.infrastructure.converter.model.ParamPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daria on 24.12.2014.
 */
public class ModifyConverter implements MessageConverter<Map<String,Object>> {


	private List<ParamPair> params;

	public ModifyConverter(List<ParamPair> params) {

		this.params = params;
	}


	@Override
	public boolean isDataSupported(Map<String, Object> data) {
		return true;
	}

	@Override
	public Map<String, Object> convert(Map<String, Object> data) {

		Map<String, Object> filledData = new HashMap<>(data);
		for(ParamPair pair: params) {
			pair.applyTo(filledData);
		}


		return filledData;
	}

}
