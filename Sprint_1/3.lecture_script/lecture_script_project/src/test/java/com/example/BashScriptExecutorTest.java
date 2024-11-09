package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BashScriptExecutorTest {

    @Test
    public void testExecuteBashSuccess() throws IOException {
        Path tempScript = Files.createTempFile("testScript", ".sh");
        try (FileWriter writer = new FileWriter(tempScript.toFile())) {
            writer.write("#!/bin/bash\n");
            writer.write("echo 'Hello, World!'\n");
        }

        tempScript.toFile().setExecutable(true);

        String output = BashScriptExecutor.executeBash(tempScript.toString());

        assertTrue(output.contains("Hello, World!"));

        Files.deleteIfExists(tempScript);
    }

    @Test
    public void testExecuteBashFileNotFound() {
        String output = BashScriptExecutor.executeBash("non_existent_script.sh");

        assertEquals("Error file not executed", output);
    }

    @Test
    public void testExecuteBashWithErrors() throws IOException {
        Path tempScript = Files.createTempFile("testScriptWithError", ".sh");
        try (FileWriter writer = new FileWriter(tempScript.toFile())) {
            writer.write("#!/bin/bash\n");
            writer.write("ech 'Should have an error'\n");
        }

        tempScript.toFile().setExecutable(true);

        String output = BashScriptExecutor.executeBash(tempScript.toString());

        assertTrue(output.contains("command not found"));

        Files.deleteIfExists(tempScript);
    }
}
