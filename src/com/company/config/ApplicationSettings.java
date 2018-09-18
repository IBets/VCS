package com.company.config;

import java.io.Serializable;

public class ApplicationSettings implements Serializable {
    public enum AppType {
        CLIENT,
        SERVER,
        MONITOR
    }
    public AppType Type;
    public ApplicationSettings(AppType type){
        Type = type;
    }
}