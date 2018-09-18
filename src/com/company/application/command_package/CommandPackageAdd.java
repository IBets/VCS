package com.company.application.command_package;

public class CommandPackageAdd extends CommandPackage {

    //Для сериализации необходим конструктор по умолчанию
    public CommandPackageAdd(){ }

    public CommandPackageAdd(String user, String repository){
        m_user = user;
        m_repository = repository;
    }

    public final String getUser(){
        return m_user;
    }

    public final String getRepository(){
        return m_repository;
    }

    public final String getCommandName() {
        return "CommandAdd";
    }

    private String m_user;
    private String m_repository;
}
