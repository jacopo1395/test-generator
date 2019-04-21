
import model.Method;
import util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    public static void main(String[] args) {
        // setup
        if (args.length < 2) {
            System.out.println("main <src dir> <target class>");
            return;
        }
        System.out.println("[START] start auto-generator");
        final String projectDirPath = args[0];
        String targetClass = args[1];
        if (!targetClass.contains(".java")) targetClass += ".java";
        File projectDir = new File(projectDirPath);
        String targetClassPath = "";
        System.out.println("[INFO] " + projectDir + " " + targetClass);

        // search files
        List<File> files = Utils.addFiles(null, projectDir);
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getName().equals(targetClass)) targetClassPath = files.get(i).getAbsolutePath();
        }
        System.out.println("[OK] target class found in the project: " + targetClassPath);

        // read class
        String code = null;
        try {
            code = Utils.readFile(targetClassPath).replaceAll("//.*\\n", "");
        } catch (IOException e) {
            System.out.println("[ERROR] class not found");
            return;
        }
        code = Utils.removeComment(code);
        List<Method> methods = null;
        try {
            methods = findMethods(code);
        } catch (Exception e) {
            System.out.println("[ERROR] problem in reading target file");
            return;
        }
        System.out.println("[DEBUG] method(s) found: " + methods.size());
        methods.stream()
                .forEach(m -> System.out.println(m.toString()));

        // creation of test files
        for (Method method : methods) {
            File testFile = new File(projectDir.getParent() + "/test/java/" + method.getPackage() + "/" + targetClass.replace(".", "Test."));
            System.out.println("[INFO] " + testFile.getPath());
            testFile.getParentFile().mkdirs();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
                writer.write("ciao");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    private static List<Method> findMethods(String code) throws Exception{
        List<Method> methods = new ArrayList<>();
        // get package
        String pack = code.split(";")[0].replace("package", "").trim();

        try {
            Pattern methodPattern = Pattern.compile(".*\\(.*\\)( )*(\\{|;)");
            Matcher methodMatcher = methodPattern.matcher(code);
            while (methodMatcher.find()) {
                // get signature of method
                String signature = methodMatcher.group();

                //remove annotation and brackets
                signature = Utils.removeAnnotation(signature);

                // get return and remove from signature
                Pattern pattern = Pattern.compile(".*<.*>");
                Matcher matcher = pattern.matcher(signature);
                matcher.find();
                String ret = matcher.group();
                signature = signature.replaceAll(ret, "");

                // get name of method and remove from signature
                String name = signature.split("\\(")[0];
                signature = signature.replace(name, "");

                // split parameters
                signature = signature.replaceFirst("\\(( )*", "");
                signature = signature.replaceFirst("\\)( )*;", "");
                String[] par = signature.split(",");
                Method method = new Method(pack, ret, name, par);
                methods.add(method);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return methods;
    }


}
