package org.ametiste.utility.xmas.infrastructure.converter.model;

import org.ametiste.utility.xmas.infrastructure.converter.ModifyConditionException;

import java.util.Map;

/**
 * Created by ametiste on 8/11/15.
 */
public class ReplaceParamPair extends AbstractParamPair {

    public ReplaceParamPair(String name, ParamValue value, ModifyCondition condition) {
        super(name, value, condition);
    }

    @Override
    public void applyTo(Map<String, Object> filledData) {
        if(filledData.containsKey(name)) {
            filledData.put(name, value.getValue());
        }
        else {
            if(condition.equals(ModifyCondition.STRICT)) {
                throw new ModifyConditionException("Expected parameter with name " + name + " to replace, but no parameter recieved, and modify conditions are strict");
            }
        }
    }
}
