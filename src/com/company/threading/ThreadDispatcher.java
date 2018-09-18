package com.company.threading;

import com.company.config.ConfigurationManager;
import com.company.config.ThreadSettings;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadDispatcher {
    private ThreadDispatcher(){
        var setting = (ThreadSettings)ConfigurationManager.getSection("Thread");
        m_monitor = new ThreadMonitor(setting.Name);
        this.add(m_monitor);

    }
    public static ThreadDispatcher getInstance(){
        while(m_instance == null)
            if(m_canWrite.compareAndSet(false, true))
                m_instance = new ThreadDispatcher();
        return m_instance;
    }
    public void add(ThreadTask task){
        var thread = new Thread(task);
        m_threads.put(thread.getId(), thread);
        thread.setName(task.getName());
        thread.start();
        m_monitor.update();
    }
    public void remove(Long threadId){
        m_threads.remove(threadId);
        m_monitor.update();
    }
    public ConcurrentHashMap<Long, Thread> getThreads(){
        return m_threads;
    }


    private static volatile ThreadDispatcher  m_instance = null;
    private static AtomicBoolean m_canWrite = new AtomicBoolean(false);
    private ConcurrentHashMap<Long, Thread> m_threads = new ConcurrentHashMap<>();
    private ThreadMonitor                   m_monitor;

}