package com.company.application.client;

public class CommandLog implements ICommand {
    CommandLog(String user, String repository, String log){
        m_user = user;
        m_repository = repository;
        m_log = log;
    }

    @Override
    public void execute() {
        System.out.println(String.format("Log: User: '%s' Repository: '%s' \n%s", m_user, m_repository, m_log));
    }

    private String m_user;
    private String m_repository;
    private String m_log;
}
