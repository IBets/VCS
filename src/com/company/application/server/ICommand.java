package com.company.application.server;

import com.company.application.command_package.CommandPackage;

import java.io.IOException;

public interface ICommand {
    CommandPackage execute() throws IOException;
}
