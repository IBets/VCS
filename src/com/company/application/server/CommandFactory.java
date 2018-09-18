package com.company.application.server;


import com.company.application.command_package.*;

class CommandFactory {
    static ICommand createInstance(CommandPackage pack){
        switch (pack.getCommandName()){
            case "CommandAdd": {
                var cmdPackage = (CommandPackageAdd)pack;
                return new CommandAdd(cmdPackage.getUser(), cmdPackage.getRepository());
            }
            case "CommandClone": {
                var cmdPackage = (CommandPackageClone)pack;
                return new CommandClone(cmdPackage.getUser(), cmdPackage.getRepository(), cmdPackage.getPath(), cmdPackage.getFlags());
            }
            case "CommandUpdate": {
                var cmdPackage = (CommandPackageUpdate)pack;
                return new CommandUpdate(cmdPackage.getUser(), cmdPackage.getRepository());
            }
            case "CommandCommit": {
                var cmdPackage = (CommandPackageCommit)pack;
                return new CommandCommit(cmdPackage.getUser(), cmdPackage.getRepository(), cmdPackage.getActualFiles(), cmdPackage.getContentData());
            }
            case "CommandRevert": {
                var cmdPackage = (CommandPackageRevert)pack;
                return new CommandRevert(cmdPackage.getUser(), cmdPackage.getRepository(), cmdPackage.getVersion(), cmdPackage.getFlags());
            }
            case "CommandLog": {
                var cmdPackage = (CommandPackageLog)pack;
                return new CommandLog(cmdPackage.getUser(), cmdPackage.getRepository());
            }
            case "CommandError": {
                var cmdPackage = (CommandPackageError)pack;
                return new CommandError(cmdPackage.getErrorMessage());
            }
            default: {
                return new CommandError("Undefined command");
            }
        }
    }
}

