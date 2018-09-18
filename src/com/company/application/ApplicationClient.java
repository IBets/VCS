package com.company.application;

import com.company.application.cli.*;
import java.util.Scanner;


public class ApplicationClient extends Application {

    public ApplicationClient(){
        m_commander.registerCommand(new CommandAdd());
        m_commander.registerCommand(new CommandClone());
        m_commander.registerCommand(new CommandCommit());
        m_commander.registerCommand(new CommandUpdate());
        m_commander.registerCommand(new CommandLog());
        m_commander.registerCommand(new CommandRevert());
        m_commander.registerCommand(new CommandExit());
   	}

    @Override
    public void run() {
        var in = new Scanner(System.in);
        while(this.isRun()) {
    	    try {
                System.out.print("-> ");
                var args = m_commander.parse(in.nextLine().split("\\s"));
                m_commander.execute(args);
            } catch (Exception e){
    	        System.out.println(e.getMessage());
            }
        }
    }

    private Commander m_commander  = new Commander();
}
