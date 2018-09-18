package com.company;
import com.company.application.*;
import com.company.config.*;


public class Program {
    public static void main(String[] args) {
        try {
            var appDesc = (ApplicationSettings)ConfigurationManager.getSection("Application");
            Application application = null;
            switch(appDesc.Type){
                case CLIENT : application = new ApplicationClient();  break;
                case SERVER : application = new ApplicationServer();  break;
                case MONITOR: application = new ApplicationMonitor(); break;
            }
            application.start();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
