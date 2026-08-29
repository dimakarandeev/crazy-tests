import helpers.DataGenerator;
import helpers.ResultAnalyser;
import helpers.ScriptRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestScenario {
    private DataGenerator dataGenerator;
    private ScriptRunner scriptRunner;
    private ResultAnalyser resultAnalyser;

    private long startTime;
    private long durationMillis;

    public TestScenario() {
        this.dataGenerator = new DataGenerator();
        this.scriptRunner = new ScriptRunner();
        this.resultAnalyser = new ResultAnalyser();
    }

    public TestScenario given() {
        System.out.println("Initializing data..");
        return this;
    }

    public TestScenario generateRecords(int num) {
        dataGenerator.generateRecords(num);
        return this;
    }

    public TestScenario withRecord(String user, String date, String category, double amount) {
        dataGenerator.withRecord(user, date, category, amount);
        return this;
    }

    public TestScenario when() {
        System.out.println("Executing actions..");
        return this;
    }

    public TestScenario executeScript() {
        scriptRunner.executeScript();
        return this;
    }

    public TestScenario executeScript(String... args) {
        scriptRunner.executeScript(args);
        return this;
    }

    public TestScenario then() {
        System.out.println("Check results..");
        return this;
    }

    public TestScenario containsRecord(String user, String month, double averageAmount) {
        resultAnalyser.containsRecord(user, month, averageAmount);
        return this;
    }

    public TestScenario containsError(String error) {
        assertEquals(error, scriptRunner.getErrorMessage().replace("\n", ""));
        return this;
    }

    public TestScenario executeScriptWithTiming() {
        startTime = System.nanoTime();
        executeScript();
        durationMillis = (System.nanoTime() - startTime) / 1_000_000;
        return this;
    }

    public TestScenario executeScriptWithTiming(String filePath) {
        startTime = System.nanoTime();
        executeScript(filePath);
        durationMillis = (System.nanoTime() - startTime) / 1_000_000;
        return this;
    }

    public TestScenario executionTimeLessThan(long limitMillis) {
        if (durationMillis == 0) {
            throw new IllegalStateException("Время не измерено. Вызовите executeScriptWithTiming() перед проверкой.");
        }
        assertTrue(durationMillis <= limitMillis,
                "Время выполнения " + durationMillis + " мс превышает лимит " + limitMillis + " мс");
        return this;
    }

    public TestScenario withUsersFile(String... logins) throws IOException {
        dataGenerator.createUsersFile(logins);
        return this;
    }

    public TestScenario withLoginsFile(Map<String, String> loginToDate) throws IOException {
        dataGenerator.createLoginsFile(loginToDate);
        return this;
    }

    public TestScenario withBannedFile(List<String> bannedUsers) throws IOException {
        dataGenerator.createBannedFile(bannedUsers);
        return this;
    }

    public TestScenario containsActiveUser(String login, String lastLogin) {
        resultAnalyser.containsRecordActiveUsers(login, lastLogin);
        return this;
    }

    public TestScenario doesNotContainActiveUser(String login) {
        resultAnalyser.containsRecordActiveUsersNotExists(login);
        return this;
    }
}