package com.company.application.command_package;


public class CommandPackageRevert extends CommandPackage {

    public CommandPackageRevert(){}

    public CommandPackageRevert(String user, String repository, String version, String flags){
        m_user = user;
        m_repository = repository;
        m_version = version;
        m_flags = flags;
    }

    public CommandPackageRevert(String user, String repository, String version, String flags, byte[] data){
        m_user = user;
        m_repository = repository;
        m_version = version;
        m_flags = flags;
        this.setContentData(data);
    }


    public final String getUser(){
        return m_user;
    }

    public final String getRepository(){
        return m_repository;
    }

    public final String getName() {
        return "CommandRevert";
    }

    public final String getVersion() { return  m_version; }

    public final String getFlags() { return m_flags; }


    private String m_user;
    private String m_repository;
    private String m_version;
    private String m_flags;

    @Override
    public String getCommandName() {
        return "CommandRevert";
    }
}
