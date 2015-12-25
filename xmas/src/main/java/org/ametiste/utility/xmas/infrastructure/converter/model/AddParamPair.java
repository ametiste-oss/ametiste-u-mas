package org.ametiste.utility.xmas.infrastructure.converter.model;

import org.ametiste.utility.xmas.infrastructure.converter.ModifyConditionException;

import java.util.Map;

/**
 * Created by ametiste on 8/11/15.
 */
public class AddParamPair extends AbstractParamPair {

    public AddParamPair(String name, ParamValue value, ModifyCondition condition) {
        super(name, value, condition);
    }

    @Override
    public void applyTo(Map<String, Object> filledData) {
        if(!filledData.containsKey(name)) {
            filledData.put(name, value.getValue());
        }
        else {
            if(condition.equals(ModifyCondition.STRICT)) {
                throw new ModifyConditionException("Expected no parameter with name " + name + " to exists to add new one, but parameter was recieved, and modify conditions are strict");
            }
        }
    }
}
