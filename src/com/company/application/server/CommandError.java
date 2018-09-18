package com.company.application.server;

import com.company.application.command_package.CommandPackage;
import com.company.application.command_package.CommandPackageError;

public class CommandError implements ICommand {

    public CommandError(String message){ m_message = message; }
    @Override
    public CommandPackage execute() {
        return new CommandPackageError(m_message);
    }
    private String m_message;
}
