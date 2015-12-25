package org.ametiste.utility.xmas.infrastructure.converter.model;

import java.util.Map;

/**
 * Created by ametiste on 8/11/15.
 */
public interface ParamPair {

    void applyTo(Map<String, Object> filledData);
}
