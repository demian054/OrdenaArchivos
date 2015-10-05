package ordenaArchivos.utils;

import java.io.File;

/**
 * Created by dbolivar on 01/10/2015.
 */
public class FileInfo {
    private String name;
    private String key;
    private File file;

    public FileInfo(File file){
        this.name = file.getName();
        String extencion = file.getName().substring(file.getName().indexOf(".")+1, file.getName().length());
        this.key = file.lastModified()+file.length()+extencion;
        this.file = file;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == FileInfo.class){
            FileInfo fileInfo = (FileInfo) obj;
            if (name.compareTo(fileInfo.getName()) == 0 || key.compareTo(fileInfo.getKey()) == 0) {
                //System.out.println("Name:"+(name.compareTo(fileInfo.getName()) == 0)+", Key:"+( key.compareTo(fileInfo.getKey()) == 0));
                return true;
            }
        }
        return false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
