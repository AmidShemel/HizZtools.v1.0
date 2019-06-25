import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static FileRedactor fileRedactor = new FileRedactor();
    public static String inputFilePatch;

    public static String tempDB = "tempDB.db";
    public static String projectsDB = "projectsDB.db";
    public static String simcoEdit = "simcoEdit.exe";
    public static String esprit = "esprit.exe";

    public static void main(String[] args) {

        inputFilePatch = args[0];
        optionsReader();

        System.out.println(simcoEdit);

        //Запуск редактора файла
        try {
            fileRedactor.fileRedactor();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "File redactor error\n" + e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        }

        //Запуск отредактированного файла в CIMCOEdit.exe
        try {
            //Process process = new ProcessBuilder("D:\\WORK\\CIMCOEdit6\\CIMCOEdit.exe", fileRedactor.outputFileFoolPatch).start();
            Process process = new ProcessBuilder(simcoEdit, fileRedactor.outputFileFoolPatch).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Program not found\n" + e,
                    "Error open file",
                    JOptionPane.ERROR_MESSAGE);
                    }
    }

    private static void optionsReader(){
        // "G:\IJ_Projects\WorkDB\settings.ini"
        //"D:\\Dropbox\\Dropbox (Work)\\Programing\\IJ_Projects\\HizZtools_final\\settings.ini"
        String line = "";
        try {
            BufferedReader optionReader = new BufferedReader (new FileReader("settings.ini"));
            while ((line = optionReader.readLine())!= null){
                switch (line){
                    case "[BDPATH]":
                        projectsDB = line = optionReader.readLine();
                        break;
                    case "[BDTEMPPATH]":
                        tempDB = line = optionReader.readLine();
                        break;
                    case "[SIMCOPATH]":
                        simcoEdit = line = optionReader.readLine();
                        break;
                    case "[ESPRITPATH]":
                        esprit = line = optionReader.readLine();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
