package com.company.threading;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadMonitor extends ThreadTask {
    public ThreadMonitor(String fileName){
        m_isRun    = new AtomicBoolean(false);
        m_isUpdate = new AtomicBoolean(false);
        m_saveFileName = fileName;
    }
    public void execute() {
        m_isRun.set(true);
        m_isUpdate.set(true);
        while(m_isRun.get()){
            if(m_isUpdate.get()) {
                try (var file = new FileWriter(m_saveFileName)) {
                    ThreadDispatcher.getInstance().getThreads().forEach((k, v) -> {
                        try {
                            file.write(String.format("ThreadName: %s \r\n", v.getName()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public String getName() {
        return "ThreadMonitor";
    }
    void update(){
        m_isUpdate.set(true);
    }

    private AtomicBoolean  m_isRun;
    private AtomicBoolean  m_isUpdate;
    private String         m_saveFileName;

}
