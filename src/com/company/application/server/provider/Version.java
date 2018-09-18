package com.company.application.server.provider;

public class Version {
    public int Major;
    public int Minor;
    public int Path;

    public Version(){
        Major = 0;
        Minor = 0;
        Path  = 0;
    }

    public Version(Version version){
        Major = version.Major;
        Minor = version.Minor;
        Path  = version.Path;
    }
    
    public Version(int major, int minor, int path){
        Major = major;
        Minor = minor;
        Path  = path;
    }

    public static Version generateNextVersion(Version version){
        var major = version.Major;
        var minor = version.Minor;
        var path  = version.Path + 1;
        if(path >= 10){
            minor += 1;
            path = 0;
        }
        if(minor >= 10){
            major += 1;
            minor = 0;
        }
        return new Version(major, minor, path);
    }

    public static Version generatePrevVersion(Version version){
        var major = version.Major;
        var minor = version.Minor;
        var path  = version.Path - 1;
        if(path < 0){
            minor -= 1;
            path = 9;
        }
        if(minor < 0){
            major -= 1;
            minor = 9;
        }
        return new Version(major, minor, path);
    }


    public static Version fromString(String data){
        var listData = data.split("\\.");
        return  new Version(Integer.parseInt(listData[0]), Integer.parseInt(listData[1]), Integer.parseInt(listData[2]));
    }

    @Override
    public final String toString() {
        return String.format("%d.%d.%d", Major, Minor, Path);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        var other = (Version)obj;
        return this.Path == other.Path && this.Major == other.Major && this.Minor == other.Minor;
    }
}
