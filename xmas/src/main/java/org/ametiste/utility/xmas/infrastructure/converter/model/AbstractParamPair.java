package org.ametiste.utility.xmas.infrastructure.converter.model;


/**
 * Created by ametiste on 8/11/15.
 */
public abstract class AbstractParamPair implements ParamPair {

    protected final String name;
    protected final ParamValue value;
    protected final ModifyCondition condition;

    public AbstractParamPair(String name, ParamValue value, ModifyCondition condition) {

        this.name = name;
        this.value = value;
        this.condition = condition;
    }

}
