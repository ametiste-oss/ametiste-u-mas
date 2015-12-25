package org.ametiste.utility.xmas.infrastructure.converter.model;

import java.util.Random;

/**
 * Created by ametiste on 8/11/15.
 */
public class RandomIntParamValue implements ParamValue {


    private final int min;
    private final int max;

    public RandomIntParamValue(int min, int max) {

        this.min = min;
        this.max = max;
    }

    @Override
    public Object getValue() {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
