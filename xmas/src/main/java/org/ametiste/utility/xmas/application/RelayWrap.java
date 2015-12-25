package org.ametiste.utility.xmas.application;

import org.ametiste.utility.xmas.domain.model.RawDataBox;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daria on 27.04.2015.
 */
public class RelayWrap {

    private RelayService service;

    public Map<String, String> run(RawDataBox dataBox) {
        service.process(dataBox);
        return new HashMap<>();
    }

    public void setService(RelayService service) {
        this.service = service;
    }
}
