package org.ametiste.utility.xmas.domain.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Daria on 25.12.2014.
 */
public class RawDataBox implements Serializable {

    private final HashMap<String, Object> map;
	private final String contentType;
    private final UUID id;

    public RawDataBox(Map<String, Object> map) {

        this.map = new HashMap<>(map);
		this.contentType = "application/json";
        this.id = UUID.randomUUID();
    }

	public RawDataBox(Map<String, Object> map, String contentType) {

		this.map = new HashMap<>(map);
		this.contentType = contentType;
        this.id = UUID.randomUUID();
	}

	public static RawDataBox buildFromMultiMap(Map<String, String[]> map) {
		Map<String, Object> singleValueMap = new HashMap<>();
        map.entrySet().stream().filter(entry -> entry.getValue() != null && entry.getValue().length != 0)
                .forEach(entry -> singleValueMap.put(entry.getKey(), entry.getValue()[0]));
		return new RawDataBox(singleValueMap, "application/x-www-form-urlencoded");
	}
    public static RawDataBox buildFromParamMap(Map<String, String[]> map) {
        Map<String, Object> singleValueMap = new HashMap<>();
        map.entrySet().stream().filter(entry -> entry.getValue() != null && entry.getValue().length != 0)
                .forEach(entry -> singleValueMap.put(entry.getKey(), entry.getValue()[0]));
        return new RawDataBox(singleValueMap);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public Object get(String key) {
        return  map.get(key);
    }

    public Map<String, Object> getAsMap() {
        return Collections.unmodifiableMap(map);
    }

    public String getContentType() {
        return contentType;
    }

    public UUID getId() {
        return id;
    }
}
