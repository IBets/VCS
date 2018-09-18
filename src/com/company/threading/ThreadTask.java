package com.company.threading;

public abstract class ThreadTask implements Runnable {
    public abstract void execute();
    public abstract String getName();
    private void finish(){
        ThreadDispatcher.getInstance().remove(Thread.currentThread().getId());
    }
    @Override
    public void run(){
        execute();
        finish();
    }
}
