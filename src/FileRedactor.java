import javax.swing.*;
import java.io.*;
import java.util.Calendar;

public class FileRedactor  {

    //private static Main main = new Main();
    public static String patch;
    public static String outputFileName;
    public static String inputFileFoolPatch;
    public static String outputFileFoolPatch;
    static String fileDate;
    static String fileTime;

    String sourceFile;

    void fileRedactor() throws IOException {

        File openFile = new File(Main.inputFilePatch);
        patch = openFile.getAbsolutePath();

        BufferedReader bReader = new BufferedReader(new FileReader(openFile));
        inputFileFoolPatch = patch.substring(0, patch.lastIndexOf("\\"));
        outputFileName = patch.substring(patch.lastIndexOf("\\"), patch.lastIndexOf("."));
        //outputFileName = setNextFileName();
        outputFileFoolPatch = inputFileFoolPatch + outputFileName + ""; // расширение файла (.*nc)
        BufferedWriter bWriter = new BufferedWriter(new FileWriter(outputFileFoolPatch));

        String line = "";
        String fileContentHead = "";
        String fileContent = "";
        String LS = System.getProperty("line.separator");
        String clear = "(                               )"; //Длинна строки
        char[] chars = new char[1];
        String newLine = "";
        String oldLine = "";
        int count = 1;

        dateAndTime();

        while (count <= 3 ){
            line = bReader.readLine();
            switch (count){
                case 2:
                    sourceFile = line.substring(line.indexOf("(")+1, line.indexOf(")"));
                    break;
            }
            fileContentHead += line + LS;
            count++;
        }

        count = 1;

        //Запись шапки
        bWriter.write(fileContentHead + LS);

        //Создание заглавия
        while ((line = bReader.readLine()) != null) {

            //Поиск названия операции
            if(line.startsWith("(#")) {
                chars = clear.toCharArray();    //Clear array
                line.getChars(2, line.length(), chars, chars.length-line.length()+2);
            }

            //Поиск номера и названия инструмента
            if(line.startsWith("N") || line.startsWith("M30")){
                if (line.startsWith("N")) {
                    String tool = line.substring(line.indexOf("T")+1, line.indexOf("T")+5);
                    String oldTool = tool;

                    tool = toolErrorMessage(tool);                          //Если номер неверен исправть, если нужно

                    if(!oldTool.equals(tool)){                              // Если номер инструмента был исправлен - заменить новым
                        line = line.replace(oldTool, tool);
                    }

                    tool.getChars(0, 4, chars, 1);                                  //Добавление № инструмента в массив
                    line.getChars(line.indexOf("T")+7, line.length()-1, chars, 6);  //Добавление имени инструмента в массив

                    newLine = String.valueOf(chars);
                }

                if(line.startsWith("M30")) newLine = "";

                if(!oldLine.equals(newLine)){
                    if(count != 1){
                        String tmp = oldLine.substring(oldLine.lastIndexOf("   ")
                                + 3, oldLine.length()-1)
                                +(" x "+ count + ")");
                        oldLine = oldLine.substring(0, oldLine.length() - tmp.length()) + tmp;
                        count = 1;
                    }

                    if(!oldLine.isEmpty()){
                        System.out.println(oldLine);               //Запись строчки в консоль
                        bWriter.write(oldLine + LS);            //Запись строчки в файл
                    }

                    oldLine = newLine;
                } else {
                    count++;
                }
            }

            fileContent = fileContent + line + LS;

        }

        System.out.println(oldLine);
        //bWriter.write(oldLine);
        bWriter.write(fileContent);
      //  bWriter.write("Source file: " + sourceFile + ".esp");
      //  bWriter.write(LS + fileDate + fileTime);
        bReader.close();
        bWriter.close();

        openFile.delete();


        writeTempFile(sourceFile + "|"
                + fileDate + "|"
                +  inputFileFoolPatch.substring(0, inputFileFoolPatch.length()-1)
                +  sourceFile + ".esp" + LS);
        //System.out.println(sourceFile + "|" + fileDate + "|" + inputFileFoolPatch +  sourceFile + ".esp");

    }

    private static void writeTempFile(String projectNC){
        try {
            FileWriter tempFileWriter = new FileWriter(Main.tempDB, true);
            tempFileWriter.write(projectNC);
            tempFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toolErrorMessage(String s) {
        char[] ch = s.toCharArray();
        if(ch[0]==ch[2] && ch[1]==ch[3]){
            return s;
        } else {
            System.out.print(s);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Tool "+s+" is incorrect, fix it?",
                    "Tool error!",
                    JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
                //System.out.println(ch);
                for(int i = 0; i < 2; i++) {
                    char index = ch[i];
                    ch[2+i] = index;
                }
                s = String.valueOf(ch);
                System.out.println(" change to " + s);
            }
            return s;
        }
    }

//    private static String toolErrorMessage(String s) {
//        char[] ch = s.toCharArray();
//        if(ch[0]==ch[2] && ch[1]==ch[3]){
//            return s;
//        } else {
//            System.out.print(s);
////            int result = JOptionPane.showConfirmDialog(
////                    null,
////                    "Tool "+s+" is incorrect, fix it?",
////                    "Tool error!",
////                    JOptionPane.YES_NO_OPTION);
////            if(result == JOptionPane.YES_OPTION){
////                //System.out.println(ch);
////                for(int i = 0; i < 2; i++) {
////                    char index = ch[i];
////                    ch[2+i] = index;
////                }
////                s = String.valueOf(ch);
////                System.out.println(" change to " + s);
////            }
//            Alert response = new Alert(Alert.AlertType.CONFIRMATION);
//                response.setTitle("Помилка інструменту" + s);
//                response.setHeaderText("");
//                response.setContentText("Можливо інструмент "+s+" неравильний, виправити його?");
//
//
//            ButtonType yes = new ButtonType("Da");
//            ButtonType no = new ButtonType("Net");
//
//            response.getButtonTypes().setAll(yes, no);
//
//            Optional<ButtonType> resalt = response.showAndWait();
//
//
//            if (resalt.get() == yes) {
//                //System.out.println(ch);
//                for(int i = 0; i < 2; i++) {
//                    char index = ch[i];
//                    ch[2+i] = index;
//                }
//                s = String.valueOf(ch);
//                System.out.println(" change to " + s);
//            } else {
//                // ... user chose NO, CANCEL, or closed the dialog
//            }
//            return s;
//        }
//    }

    private static void dateAndTime(){
        //  (01.08.2018 15:33:35)
        Calendar c = Calendar.getInstance();
        fileTime = String.format("%tT", c);
        fileDate = String.format("%ta %td.%tm.%ty", c, c, c, c);
        System.out.printf("Date: %ta %td.%tm.%ty p.%n", c, c, c, c);
        System.out.printf("Time: %tT%n", c);
    }

    // Метод генерує им'я файлу більше на один від найбільшого (до 1000)
    private static String setNextFileName() {

        File folder = new File(inputFileFoolPatch);
        File[] listOfFiles = folder.listFiles();
        String fileName;
        int j, k = 0;

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                j = Integer.valueOf(listOfFiles[i].getName().toUpperCase().replaceAll("[^0-9]", ""));
                if(j>999){j = 0;}
                if(k<j){k = j;}
            } else if (listOfFiles[i].isDirectory()) {
                //System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        fileName = String.format("\\O%04d", k + 1);
        return fileName;
    }
}
