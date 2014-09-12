package com.chooblarin.twooblarin.event;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by chooblarin on 2014/09/13.
 */
public class BusHolder {
    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return BUS;
    }

    private BusHolder(){
    }
}
