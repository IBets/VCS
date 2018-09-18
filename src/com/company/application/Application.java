package com.company.application;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Application {
    protected abstract void run() throws Exception;

    protected void shutdown(){
        m_isRun.set(false);
    }

    boolean isRun(){
        return m_isRun.get();
    }

    public void start() throws Exception {

        Runtime.getRuntime().addShutdownHook(new Thread(this::finish));
        m_isRun.set(true);
        m_isExit.set(false);
        run();
        m_isRun.set(false);
        m_isExit.set(true);
    }

    private void finish()  {
        shutdown();
        var clock = 100000000.0;
        while (!m_isExit.get() && clock > 0){ clock -= 0.1; }

    }

    private AtomicBoolean m_isRun  = new AtomicBoolean(false);
    private AtomicBoolean m_isExit = new AtomicBoolean(false);

}
