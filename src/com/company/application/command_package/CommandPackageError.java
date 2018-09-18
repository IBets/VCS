package com.company.application.command_package;

public class CommandPackageError extends CommandPackage {

    public CommandPackageError(){ }

    public CommandPackageError(String message){
        m_message = message;
    }

    public final String getErrorMessage(){
        return m_message;
    }

    public final String getCommandName() {
        return "CommandError";
    }
    private String m_message;
}
