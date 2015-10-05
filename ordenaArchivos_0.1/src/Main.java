import ordenaArchivos.utils.ReadFiles;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;

import java.io.File;
import java.util.Scanner;

public class Main {



    public static void main(String[] args) {
        try {

            System.out.println("Hello World!");
            String beginFile = "";

            if (args.length>0){
                beginFile = args[0];
            } else {
                beginFile = new File(".").getCanonicalPath();
            }
            System.out.println(beginFile);

            ReadFiles readFiles = new ReadFiles(new File(beginFile));

            readFiles.read();
            readFiles.deleteDuplicatedFiles();
            //readFiles.orderFilesByDate("/yyyy/MM/");
            readFiles.orderFilesByDate("/MM/dd/");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
