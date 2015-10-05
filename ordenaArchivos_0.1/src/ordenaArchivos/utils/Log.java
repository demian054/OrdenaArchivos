package ordenaArchivos.utils;

import java.io.*;

/**
 * Created by dbolivar on 01/10/2015.
 */
public class Log {

    private String logName = "_Log.txt";
    private String path;

    public Log(String path){
        this.path = path;
    }

    public void printLog(String message){
        FileWriter logFW = null;
        try {
            File log = new File(path + logName);

            if (!log.exists()) {
                log.getParentFile().mkdirs();
                log.createNewFile();
            }

            String oldMesaje = readFile(log.getAbsolutePath());
            writeFile(log.getAbsolutePath(),oldMesaje+"\n"+message);

            System.out.println(message);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (null != logFW){
                    logFW.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void printLog(String message, Exception e){
        e.printStackTrace();
        printLog(message);
    }

    public static String readFile(String filename) throws IOException {
        File file = new File(filename);

        StringBuilder oldMessage= new StringBuilder();

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        do {
            line = br.readLine();
            if (line!=null){
               oldMessage.append(line).append("\n");

            }
        } while (line!=null);

        return oldMessage.toString();
    }

    public static void writeFile(String filename, String text) throws IOException {
        File file = new File(filename);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text);
        bw.close();
    }

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch(IOException ignored) {
        }
    }


}
