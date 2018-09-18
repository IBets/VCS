package com.company.application.command_package;

public class CommandPackageClone extends CommandPackage {

    public CommandPackageClone(){ }

    public CommandPackageClone(String user, String repository, String path, String flags){
        m_user = user;
        m_repository = repository;
        m_path = path;
        m_flags = flags;
    }

    public CommandPackageClone(String user, String repository, String path, String flags,  byte[] data){
        m_user = user;
        m_repository = repository;
        m_path = path;
        m_flags = flags;
        this.setContentData(data);
    }

    public final String getUser(){
        return  m_user;
    }

    public final String getRepository(){
        return m_repository;
    }

    public final String getPath(){
        return  m_path;
    }

    public final String getFlags(){
        return m_flags;
    }

    public final String getCommandName() {
        return "CommandClone";
    }

    private String m_user;
    private String m_repository;
    private String m_path;
    private String m_flags;
}
