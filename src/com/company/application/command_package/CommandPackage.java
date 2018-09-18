package com.company.application.command_package;

public abstract class CommandPackage {
    public abstract String  getCommandName();
    public byte[] getContentData(){
       return m_data;
    }
    public void   setContentData(byte[] data){
        m_data = data;
    }
    public byte[] resetContentData(){
        var data = getContentData();
        setContentData(new byte[0]);
        return data;
    }
    private byte[] m_data = new byte[0];
}
