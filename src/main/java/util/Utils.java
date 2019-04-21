package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    // File Utils
    public static String readFile(String filePath) throws IOException {
        BufferedReader reader;
        String content = "";
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                content += line + "\n";
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return content;
    }

    public static List<File> addFiles(List<File> files, File dir) {
        if (files == null)
            files = new LinkedList<File>();

        if (!dir.isDirectory()) {
            files.add(dir);
            return files;
        }

        for (File file : dir.listFiles())
            addFiles(files, file);
        return files;
    }

    // code parsing utils
    public static String removeAnnotation(String s) {
        int openBrackets = 0;
        boolean ignoreBrackets = true; // ignore brackets before annotation
        boolean opened = false;
        boolean append = true;
        s = s.replaceAll("\\\".*\\\"", "");
        StringBuilder ret = new StringBuilder("");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '@') {
                append = false;
                ignoreBrackets = false;
            }
            if (!ignoreBrackets && c == '(') {
                opened = true;
                openBrackets++;
            }
            if (!ignoreBrackets && c == ')') openBrackets--;
            if (!ignoreBrackets && opened && openBrackets == 0) {
                append = true;
                ignoreBrackets = true;
                c = '\0';
            }
            if (!append && !opened && c == ' ' ) {
                while(c==' '){
                    c = s.charAt(i++);
                }
                if(c!='(') {
                    append = true;
                    ignoreBrackets = true;
                }
            }
            if (append) ret.append(c);
        }
        return String.valueOf(ret);
    }

    public static String removeComment(String s) {
        boolean append = true;
        StringBuilder ret = new StringBuilder("");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '/' && i+1<s.length() && s.charAt(i+1)=='*') {
                append = false;
            }
            if (c == '/' && i-1>=0 && s.charAt(i-1)=='*') {
                append = true;
                c='\0';
            }
            if (append) ret.append(c);
        }
        return String.valueOf(ret);
    }
}
