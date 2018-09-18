package com.company.file_worker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FileWorker {
    public FileWorker(String pathSource, boolean recursive) {
        m_pathSource = Paths.get(pathSource);
        m_recursive  = recursive;
    }

    public Map<String, String> execute(IExecutable command) throws Exception {
        var hash = new HashMap<String, String>();
        if(isRecursive()){
           var files = Files.walk(m_pathSource)
                    .filter(Files::isRegularFile)
                    .map(Path::toAbsolutePath)
                    .collect(Collectors.toList());
            for(var e : files)
                if(!m_pathSource.relativize(e).toString().startsWith(".~"))
                    hash.put(m_pathSource.relativize(e).toString(), command.process(e.toFile()));

        } else {
            var files = Files.list(m_pathSource)
                    .filter(Files::isRegularFile)
                    .map(Path::toAbsolutePath)
                    .collect(Collectors.toList());
            for(var e : files)
                if(!m_pathSource.relativize(e).toString().startsWith(".~"))
                    hash.put(m_pathSource.relativize(e).toString(), command.process(e.toFile()));
        }

        return hash;
    }

    public boolean isRecursive() {
        return m_recursive;
    }

    public void setRecursive(boolean flag) {
        m_recursive = flag;
    }

    private boolean m_recursive;
    private Path    m_pathSource;
}
