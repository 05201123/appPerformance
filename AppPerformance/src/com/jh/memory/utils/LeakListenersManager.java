package com.jh.memory.utils;


import java.util.ArrayList;
import java.util.List;
/**
 * 泄漏监听管理者
 * @author 099
 *
 */
public class LeakListenersManager {

    private static LeakListenersManager sInstance;

    List<LeakListener> listeners = new ArrayList<LeakListener>();

    private LeakListenersManager() {

    }

    public static LeakListenersManager getManager() {
        if (sInstance == null)
            sInstance = new LeakListenersManager();

        return sInstance;
    }
    public void addListener(LeakListener listener) {
        listeners.add(listener);
    }

    public void removeListener(LeakListener listener) {
        listeners.remove(listener);
    }

}
