package helpers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultAnalyser {
    private static final String RESULT_FILE_ACTIVE_USER =
            Paths.get(System.getProperty("user.dir"), "script", "active_users.csv").toString();
    private List<String[]> records = new LinkedList<>();

    public void containsRecord(String user, String month, double averageAmount) {
        readRecords(RESULT_FILE_ACTIVE_USER);

        String[] userRecord = records.stream()
                .filter(record -> record.length >= 3 && record[0].equals(user))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Пользователь '" + user + "' не найден"
                ));

        assertEquals(month, userRecord[1]);
        assertEquals(averageAmount, Double.parseDouble(userRecord[2]));
    }

    private void readRecords(String pathFile) {
        records.clear();

        try {
            List<String> lines = Files.readAllLines(Paths.get(pathFile), StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                records.add(parts);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл: " + pathFile, e);
        }
    }

    public void containsRecordActiveUsers(String login, String lastLogin) {
        readRecords(RESULT_FILE_ACTIVE_USER);

        String[] userRecord = records.stream()
                .filter(record -> record.length >= 2 && record[0].equals(login))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Пользователь '" + login + "' не найден. Содержимое: " +
                                Arrays.deepToString(records.toArray())
                ));

        assertEquals(lastLogin, userRecord[1]);
    }

    public void containsRecordActiveUsersNotExists(String login) {
        readRecords(RESULT_FILE_ACTIVE_USER);

        boolean found = records.stream().anyMatch(record -> record[0].equals(login));
        if (found) throw new AssertionError("Пользователь " + login + " присутствует, а не должен");
    }
}