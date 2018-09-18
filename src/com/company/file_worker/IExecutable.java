package com.company.file_worker;

import java.io.File;
import java.io.IOException;

public interface IExecutable {
    String process(File file)       throws Exception;
    String process(String fileData) throws Exception;
    String process(byte[] fileData) throws Exception;
}
