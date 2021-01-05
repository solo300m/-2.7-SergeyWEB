package org.example.web.dto;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FileSecurity {
    private static volatile FileSecurity instatnt;
    private HashSet<File> hashSet;

    private FileSecurity(){
        this.hashSet = new HashSet<>();
    }
    private FileSecurity(File dir) {
        List<File> listOfFile = Arrays.asList(dir.listFiles());
        hashSet = new HashSet<>();
        if(listOfFile.size()!=0) {
            for(File f: listOfFile){
                if(f.isFile())
                    hashSet.add(f.getAbsoluteFile());
            }
        }
    }
    public static FileSecurity getInstance(){
        if(instatnt == null){
            instatnt = new FileSecurity();
        }
        return instatnt;
    }

    public static FileSecurity getInstance(File dir){
        if(instatnt == null){
            instatnt = new FileSecurity(dir);
        }
        return instatnt;
    }

    public boolean containsFile(File file){
        for(File f: hashSet){
            if(f.getName().equals(file.getName()))
                return true;
        }
        return false;
    }

    public boolean addFile(File file){
        if(file.isFile()) {
            hashSet.add(file);
            return true;
        }
        else
            return false;
    }

    @Override
    public String toString() {
        StringBuilder sB = new StringBuilder();
        for(File f: hashSet){
            sB.append(f.getAbsolutePath().toString()+"\n");
        }
        String rez = sB.toString();
        return rez;
    }
}
