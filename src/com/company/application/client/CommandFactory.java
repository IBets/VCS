package com.company.application.client;

import com.company.application.command_package.*;

public class CommandFactory {
    public static ICommand createInstance(CommandPackage pack){
        var cmdName = pack.getCommandName();
        switch (cmdName){
            case "CommandAdd":{
                var cmdPackage = (CommandPackageAdd)pack;
                return new CommandAdd(cmdPackage.getUser(), cmdPackage.getRepository());
            }
            case "CommandClone":{
                var cmdPackage = (CommandPackageClone)pack;
                return new CommandClone(cmdPackage.getUser(), cmdPackage.getRepository(), cmdPackage.getPath(), cmdPackage.getFlags(), cmdPackage.getContentData());
            }
            case "CommandCommit":{
                var cmdPackage = (CommandPackageCommit)pack;
                return new CommandCommit(cmdPackage.getUser(), cmdPackage.getRepository());
            }
            case "CommandUpdate":{
                var cmdPackage = (CommandPackageUpdate)pack;
                return new CommandUpdate(cmdPackage.getUser(), cmdPackage.getRepository(), cmdPackage.getContentData());
            }
            case "CommandRevert":{
                var cmdPackage = (CommandPackageRevert)pack;
                return new CommandRevert(cmdPackage.getUser(), cmdPackage.getRepository(), cmdPackage.getVersion(), cmdPackage.getFlags(), cmdPackage.getContentData());
            }
            case "CommandLog":{
                var cmdPackage = (CommandPackageLog)pack;
                return new CommandLog(cmdPackage.getUser(), cmdPackage.getRepository(), cmdPackage.getLog());
            }
            case "CommandError":{
                var cmdPackage = (CommandPackageError)pack;
                return new CommandError(cmdPackage.getErrorMessage());
            }
            default: {
                return new CommandError("Error undefined command");
            }
        }
    }
}
