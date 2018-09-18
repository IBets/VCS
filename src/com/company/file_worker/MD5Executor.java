package com.company.file_worker;

import org.apache.commons.codec.digest.DigestUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class MD5Executor implements IExecutable {
    @Override
    public String process(File file) throws IOException {
        if(file.isFile()) {
            try (var is = new FileInputStream(file)) {
                return DigestUtils.md5Hex(is);
            }
        } else {
            return DigestUtils.md5Hex("DIRECTORY");
        }
    }
    public String process(String fileData)  {
         return DigestUtils.md5Hex(fileData);
    }

    public String process(byte[] fileData)  {
        return DigestUtils.md5Hex(fileData);
    }
}