package io.jenkins.plugins.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BashScriptExecutor {

    public static String executeBash(String file) {
        try {
            List<String> command = new ArrayList<>();
            command.add(file);
            System.out.println("1");

            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.start();
            System.out.println("2");
            // Lecture de la sortie standard du processus
            try (BufferedReader stdInput = new BufferedReader(
                            new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                    BufferedReader stdError = new BufferedReader(
                            new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {

                StringBuilder output = new StringBuilder();
                String line;
                System.out.println("4");
                while ((line = stdInput.readLine()) != null) {
                    System.out.println("5");
                    output.append(line).append("\n");
                }

                while ((line = stdError.readLine()) != null) {
                    System.out.println("6");
                    output.append(line).append("\n");
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    // Le script a retourné un code de sortie non nul, indiquant une erreur
                    return "Script execution failed with exit code " + exitCode;
                }

                return output.toString();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error file not executed";
        }
    }

    public static void main(String[] args) {
        String user_path = System.getProperty("user.dir");
        String path = user_path + "/work/workspace/bash.sh";
        String content = BashScriptExecutor.executeBash(path);
        System.out.println(content);
    }
}
