package org.ametiste.utility.xmas.infrastructure.converter.model;

/**
 * Created by ametiste on 8/11/15.
 */
public class ConstantParamValue implements ParamValue {


    private Object value;

    public ConstantParamValue(Object value) {

        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
