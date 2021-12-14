import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class JavFol {
    private static String resultData = "";

    public static void main(String[] args) throws IOException, InterruptedException {
        File folder = new File(args[0]);
        File[] listOfFiles = folder.listFiles();
        readFolder(listOfFiles);
        // System.out.println(resultData);

        try {
            FileWriter myWriter = new FileWriter(args[0] + ".java");
            myWriter.write(resultData);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Process pr = Runtime.getRuntime().exec("java " + args[0] + ".java");
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while ((line = buf.readLine()) != null) {
            System.out.println(line);
        }

        File myObj = new File(args[0] + ".java");
        myObj.delete();
    }

    private static void readFolder(File[] listOfFiles) {
        sortListOfFiles(listOfFiles);
        for (int i = 0; i < listOfFiles.length; i++) {
            String line = listOfFiles[i].getName();
            String data = line.substring(line.indexOf(":") + 1);

            Boolean closeBracket = false;
            if (data.charAt(data.length() - 1) == ';')
                resultData += data;
            else {
                resultData += data + "{";
                closeBracket = true;
            }

            if (listOfFiles[i].listFiles() != null &&
                    listOfFiles[i].listFiles().length != 0)
                readFolder(listOfFiles[i].listFiles());
            if (closeBracket)
                resultData += "}";
        }
    }

    private static File[] sortListOfFiles(File[] listOfFiles) {
        for (int i = 0; i < listOfFiles.length - 1; i++) {
            for (int j = i + 1; j < listOfFiles.length; j++) {
                if (Float.parseFloat(
                        listOfFiles[i].getName().substring(0, listOfFiles[i].getName().indexOf(":"))) > Float
                                .parseFloat(
                                        listOfFiles[j].getName().substring(0, listOfFiles[j].getName().indexOf(":")))) {

                    File tmp = listOfFiles[i];
                    listOfFiles[i] = listOfFiles[j];
                    listOfFiles[j] = tmp;
                }
            }
        }

        return listOfFiles;
    }
}