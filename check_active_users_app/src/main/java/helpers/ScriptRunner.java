package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScriptRunner {

    private static final Path BASE_DIR = Paths.get(System.getProperty("user.dir"));
    private static final Path DATA_DIR = BASE_DIR.resolve("script/data");

    private static final String BASH_PATH = "C:/Program Files/Git/bin/bash.exe";
    private static final String SCRIPT_NAME = "./check_active_users.sh";

    private static final String FILE_PATH_BANNED = DATA_DIR.resolve("banned.json").toString();
    private static final String FILE_PATH_LOGINS = DATA_DIR.resolve("logins.csv").toString();
    private static final String FILE_PATH_USERS = DATA_DIR.resolve("users.txt").toString();

    private String errorMessage;
    private int exitCode;

    public void executeScript() {
        runScript(FILE_PATH_USERS, FILE_PATH_LOGINS, FILE_PATH_BANNED);
    }

    public void executeScript(String... args) {
        runScript(args);
    }

    private void runScript(String... args) {
        System.out.println("Running script..");

        List<String> command = new ArrayList<>();
        command.add(BASH_PATH);
        command.add(SCRIPT_NAME);
        command.addAll(Arrays.asList(args));

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(System.getProperty("user.dir"), "script"));
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            StringBuilder outputBuilder = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
            }

            errorMessage = outputBuilder.toString();
            exitCode = process.waitFor();

        } catch (IOException | InterruptedException e) {
            errorMessage = e.getMessage();
        }
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}