package com.example;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class BashScriptExecutor {

    public static void main(String[] args) {
        executeBash("./lib/infos_systeme.sh");
    }

    public static String executeBash(String file) {
        try {

            //String file = "./lib/infos_systeme.sh";
            List<String> command = new ArrayList<>();
            command.add(file);


            ProcessBuilder builder = new ProcessBuilder(command);
            //builder.directory(new File("/lib"));
            Process process = builder.start();


            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));


            String line;
            String lines="";
            while ((line = stdInput.readLine()) != null) {
                lines+=line;
                System.out.println(line);
            }
            

            while ((line = stdError.readLine()) != null) {
                System.err.println(line);
                lines+=line;
            }

            process.waitFor();
            return lines;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error file not executed";
        }
    }
}
