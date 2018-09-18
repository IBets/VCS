package com.company.application.command_package;

public class CommandPackageUpdate extends CommandPackage {

    public CommandPackageUpdate(){ }

    public CommandPackageUpdate(String user, String repository){
        m_user = user;
        m_repository = repository;
    }

    public CommandPackageUpdate(String user, String repository, byte[] data){
        m_user = user;
        m_repository = repository;
        this.setContentData(data);
    }

    public final String getUser(){
        return  m_user;
    }

    public final String getRepository(){
        return m_repository;
    }

    @Override
    public String getCommandName() {
        return "CommandUpdate";
    }

    private String m_user;
    private String m_repository;
}
