package com.company.application.client;

public class CommandCommit implements ICommand {

    CommandCommit(String user, String repository){
        m_user = user;
        m_repository = repository;
    }

    @Override
    public void execute() {
        System.out.println(String.format("Success commit. User: '%s' Repository: '%s'", m_user, m_repository));
    }
    private String m_user;
    private String m_repository;

}
