package com.company.application.client;

public class CommandError implements ICommand {

    CommandError(String message){
        m_message = message;
    }

    @Override
    public void execute() {
        System.out.println(String.format("Error: %s", m_message));
    }
    private String m_message;
}
