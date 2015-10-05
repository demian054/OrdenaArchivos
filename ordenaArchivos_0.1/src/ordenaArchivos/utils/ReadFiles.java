package ordenaArchivos.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by dbolivar on 01/10/2015.
 */
public class ReadFiles {

    private Log log;


    private String duplicatePath = "Duplicated";
    private String orderByDatePath = "ByDate";
    private String logPath = "Logs";
    private String levelSeparator = "    ";
    private boolean deleteEmptyFiles = true;

    private ArrayList<FileInfo> scanFileList = new ArrayList<FileInfo>();
    private ArrayList<FileInfo> scanFileListDuplicate = new ArrayList<FileInfo>();
    private ArrayList<String>   preferPath = new ArrayList<String>();
    private File path;

    public ReadFiles(File path){

        this.path = path;
        log = new Log(path+File.separator+logPath+File.separator+ReadFiles.class.getName());
    }

    public void read () throws IOException {
        Scanner scanner = new Scanner(System.in);
        read(path, scanner, levelSeparator);
    }

    private void read (File file, Scanner scanner, String level) throws IOException {

        if (!isEmptyFiles(file)){
            for (File element : file.listFiles()) {
                if (element.isDirectory()) {
                    if (!isFolderSystem(element, level)) {
                        log.printLog(level + element.getAbsolutePath() + ".......");
                        read (element, scanner, level+levelSeparator);
                    }
                } else {
                    int indexOfDuplicatedFile = scanFileList.indexOf(new FileInfo(element));
                    if (indexOfDuplicatedFile == -1) {
                        scanFileList.add(new FileInfo(element));
                        log.printLog(new Date(element.lastModified()).toString()+level + element.getName());
                    } else {
                        File oldFile = scanFileList.get(indexOfDuplicatedFile).getFile();

                        if (preferPath.indexOf(oldFile.getParent())== -1 && preferPath.indexOf(element.getParent())== -1){
                            log.printLog(level + "archivos repetidos " + element.getName() + ", cual archivo conservamos?? /n 1:" + element.getParent() + " /n  2:" + oldFile.getParent());
                            //String preferPath = "";
                            String resp = "";
                            while (resp.equalsIgnoreCase("")){
                                resp = scanner.next();
                                if (resp.equalsIgnoreCase("1")){
                                    preferPath.add(element.getParent());
                                } else if (resp.equalsIgnoreCase("2")){
                                    preferPath.add(oldFile.getParent());
                                } else {
                                    resp = "";
                                    log.printLog(level + "Opcion Invalida intente nuevamente:");
                                }
                            }
                        }
                        if (preferPath.contains(oldFile.getParent())) {
                            scanFileListDuplicate.add(new FileInfo(element));
                            log.printLog(level + element.getName() + " REP-> " + oldFile.getParent());
                        } else if (preferPath.contains(element.getParent())){
                            scanFileList.remove(new FileInfo(oldFile));
                            scanFileList.add(new FileInfo(element));
                            scanFileListDuplicate.add(new FileInfo(oldFile));
                            log.printLog(level + element.getName() + " REP-> " + element.getParent());
                        }
                    }
                }
            }
        }
    }
    private boolean isFolderSystem(File file, String level){
        if (file.getName().equalsIgnoreCase(duplicatePath)){
            log.printLog(level + file.getAbsolutePath() + "                        <-----Archivo para respaldos----->");
            return true;
        } else if (file.getName().equalsIgnoreCase(orderByDatePath)){
            log.printLog(level + file.getAbsolutePath() + "                        <-----Archivo para Ordenamiento por fechas----->");
            return true;
        } else if (file.getName().equalsIgnoreCase(logPath)){
            log.printLog(level + file.getAbsolutePath() + "                        <-----Archivo para Ordenamiento por fechas----->");
            return true;
        }
        return false;
    }

    public int deleteDuplicatedFiles() {
        log.printLog("Borrando Duplicados........");
        int r = 0;
        File duplicatedFolder = new File(path.getAbsolutePath()+File.separator+duplicatePath);
        duplicatedFolder.mkdirs();
        log.printLog(duplicatedFolder.getAbsolutePath());
        File duplicatedFile;
        for (FileInfo toDelete:scanFileListDuplicate){
            String duplicatedFileName = duplicatedFolder.getAbsolutePath()+File.separator+toDelete.getName();
            duplicatedFile = new File(duplicatedFileName);
            File file = toDelete.getFile();
            int i =1;
            while (duplicatedFile.exists()){
                i++;
                duplicatedFile = new File(duplicatedFileName+"_"+i);
            }
            if (file.renameTo(duplicatedFile)){
                log.printLog("Move -> "+file.getAbsolutePath()+"   ->   "+duplicatedFile.getAbsolutePath());
                r++;
            } else {
                log.printLog("Error-> "+file.getAbsolutePath()+"   ->   "+duplicatedFile.getAbsolutePath());
            }
        }
        log.printLog(r+" Duplicados eliminados");
        return r;
    }

    private boolean isEmptyFiles(File file) {
        if (file.listFiles().length == 0) {
            log.printLog(" Archivo vacio");
            if (deleteEmptyFiles) {
                file.delete();
                log.printLog(" Archivo Borrado");
            }
            return  true;
        }
        return  false;
    }

    public int orderFilesByDate(String format) {
        int r = 0;
        File folderResult = new File(path.getAbsolutePath()+File.separator+orderByDatePath);
        folderResult.mkdirs();
        log.printLog(folderResult.getAbsolutePath());
        File orderFile;
        for (FileInfo toOrder:scanFileList){
            Date dateFolder = new Date(toOrder.getFile().lastModified());

            String dateFolderPath = new SimpleDateFormat(format.replace("/",File.separator)).format(dateFolder);
            String duplicatedFileName = folderResult.getAbsolutePath()+dateFolderPath+toOrder.getName();
            orderFile = new File(duplicatedFileName);
            orderFile.getParentFile().mkdirs();
            File file = toOrder.getFile();
            int i =1;
            while (orderFile.exists()){
                i++;
                orderFile = new File(duplicatedFileName+"_"+i);
            }
            if (file.renameTo(orderFile)){
                log.printLog("Move -> "+file.getAbsolutePath()+"   ->   "+orderFile.getAbsolutePath());
                isEmptyFiles(file.getParentFile());
                r++;
            } else {
                log.printLog("Error-> "+file.getAbsolutePath()+"   ->   "+orderFile.getAbsolutePath());
            }
        }
        return r;
    }



}

