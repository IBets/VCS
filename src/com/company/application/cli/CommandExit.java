package com.company.application.cli;

import com.beust.jcommander.*;

@Parameters(commandNames = {"exit"}, commandDescription = "Exit command")
public class CommandExit implements ICommand {
    @Override
    public void execute() {
        System.exit(0);
    }
    @Override
    public String getName() {
        return "exit";
    }
}
