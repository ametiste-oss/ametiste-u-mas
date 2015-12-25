package org.ametiste.utility.xmas.infrastructure.converter.model;

import org.ametiste.utility.xmas.infrastructure.converter.ModifyConditionException;

import java.util.Map;

/**
 * Created by ametiste on 8/11/15.
 */
public class DeleteParamPair extends AbstractParamPair {

    public DeleteParamPair(String name, ParamValue value, ModifyCondition condition) {
        super(name, value, condition);
    }

    @Override
    public void applyTo(Map<String, Object> filledData) {
        if(filledData.containsKey(name)) {
            filledData.remove(name);
        }
        else {
            if(condition.equals(ModifyCondition.STRICT)) {
                throw new ModifyConditionException("Expected parameter with name " + name + " to exists to remove, but parameter doesnt exist, and modify conditions are strict");
            }
        }
    }
}
