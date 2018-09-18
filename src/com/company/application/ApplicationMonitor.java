package com.company.application;

import com.company.config.ConfigurationManager;
import com.company.config.ThreadSettings;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApplicationMonitor extends Application {

    @Override
    public void run() {
        var fileName = ((ThreadSettings)ConfigurationManager.getSection("Thread")).Name;
        var formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        var frame = new JFrame("ThreadMonitor");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var textArea = new JTextArea();
        textArea.setSize(800, 600);
        textArea.setDoubleBuffered(true);
        frame.getContentPane().add(textArea);
        frame.pack();
        frame.setVisible(true);

        try{
            while (this.isRun()) {
                var now = LocalDateTime.now();
                var data = new StringBuilder();

                data.append(String.format("Time: %s \n", formatter.format(now)));
                Files.lines(Paths.get(fileName), StandardCharsets.UTF_8).forEach((e)->data.append(e + "\r\n"));
                textArea.setText(data.toString());
                Thread.sleep(100);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
