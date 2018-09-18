package com.company.application.cli;

import com.beust.jcommander.*;
import java.util.HashMap;
import java.util.Map;

public class Commander {
    public void registerCommand(ICommand cmd){
        m_map.put(cmd.getName(), cmd);
        m_commander.addCommand(cmd);
    }
    public String parse(String[] args){
        m_commander.parse(args);
        return m_commander.getParsedCommand();
    }
    public void execute(String cmd) {
        m_map.get(cmd).execute();
        m_commander = new JCommander();
        m_map.forEach((k, v)-> m_commander.addCommand(v));
    }

    private Map<String, ICommand> m_map  = new HashMap<>();
    private JCommander m_commander = new JCommander();
}
