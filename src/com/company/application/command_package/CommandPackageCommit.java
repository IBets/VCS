package com.company.application.command_package;

public class CommandPackageCommit extends CommandPackage {

    public CommandPackageCommit(){}

    public CommandPackageCommit(String user, String repository){
        m_user = user;
        m_repository = repository;
    }

    public CommandPackageCommit(String user, String repository, String[] actualFiles, byte[] data){
        m_user = user;
        m_repository = repository;
        m_actualFiles = actualFiles;
        this.setContentData(data);
    }

    public final String[] getActualFiles(){
        return m_actualFiles;
    }

    public final String getUser(){
        return  m_user;
    }

    public final String getRepository(){
        return m_repository;
    }

    public final String getCommandName() {
        return "CommandCommit";
    }

    private String   m_user;
    private String   m_repository;
    private String[] m_actualFiles;
}
