package com.opencharge.opencharge.presentation.locators;

import com.opencharge.opencharge.domain.executor.Executor;
import com.opencharge.opencharge.domain.executor.MainThread;
import com.opencharge.opencharge.domain.executor.impl.ThreadExecutor;
import com.opencharge.opencharge.threading.MainThreadImpl;

/**
 * Created by ferran on 22/3/17.
 */

public class ServicesLocator {
    private static ServicesLocator instance;

    private ServicesLocator() {}

    public static ServicesLocator getInstance() {
        if(instance == null) {
            instance = new ServicesLocator();
        }
        return instance;
    }

    public MainThread getMainThread() {
        return MainThreadImpl.getInstance();
    }

    public Executor getExecutor() {
        return ThreadExecutor.getInstance();
    }
}
