package com.company.application.command_package;


public class CommandPackageLog extends CommandPackage {

    public CommandPackageLog (){ }

    public CommandPackageLog(String user, String repository){
        m_user = user;
        m_repository = repository;
    }

    public CommandPackageLog(String user, String repository, String log){
        m_user = user;
        m_repository = repository;
        m_log = log;
    }

    public final String getUser(){
        return  m_user;
    }

    public final String getRepository(){
        return m_repository;
    }

    public final String getLog(){
        return m_log;
    }

    @Override
    public String getCommandName() {
         return "CommandLog";
    }

    private String m_user;
    private String m_repository;
    private String m_log;
}
