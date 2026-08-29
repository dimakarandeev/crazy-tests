package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataGenerator {

    private static final Path BASE_DIR = Paths.get(System.getProperty("user.dir"));
    private static final Path DATA_DIR = BASE_DIR.resolve("script/data");

    private static final String DIR_PATH = Paths.get(System.getProperty("user.dir"), "script", "data").toString();
    private static final String FILE_PATH = Paths.get(System.getProperty("user.dir"), "script", "data", "transactions.csv").toString();

    private static final String FILE_PATH_BANNED = DATA_DIR.resolve("banned.json").toString();
    private static final Path FILE_PATH_LOGINS = DATA_DIR.resolve("logins.csv");
    private static final Path FILE_PATH_USERS = DATA_DIR.resolve("users.txt");

    private static final String[] USERS = {"alice", "bob", "charlie", "tom"};
    private static final String[] CATEGORIES = {"food", "transport", "beauty"};

    private void createDir() {
        File directory = new File(DIR_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private String generateRandomDate() {
        Random random = new Random();
        int month = random.nextInt(12) + 1; // [0,11] +1 -> [1, 12]
        int day = random.nextInt(28) + 1; // [1,29]
        return String.format("2025-%02d-%02d", month, day);
    }

    private void writeData(String data, boolean append) {
        try(FileWriter writer = new FileWriter(FILE_PATH, append)) {
            writer.write(data);
            System.out.println("Data was written to file " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error while writing data to file " + e.getMessage());
        }
    }

    private String generateRecordString(int num) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("user,date,category,amount\n");

        Random random = new Random();

        for (int i = 0; i < num; i++) {
            String user = USERS[random.nextInt(USERS.length)];
            String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
            String date = generateRandomDate();
            double amount = 1900 * random.nextDouble() + 100;
            stringBuilder.append(String.format("%s,%s,%s,%.2f\n", user, date, category, amount));
        }
        return stringBuilder.toString();
    }

    public void generateRecords(int num) {
        createDir();
        String generatedRecords = generateRecordString(num);
        writeData(generatedRecords, false);
    }

    public void withRecord(String user, String date, String category, double amount) {
        String record = String.format("%s,%s,%s,%.2f\n", user, date, category, amount);

        writeData(record, true);
    }

    public void createUsersFile(String... logins) throws IOException {
        String content = String.join("\n", logins) + "\n";
        Files.write(FILE_PATH_USERS, content.getBytes(StandardCharsets.UTF_8));
    }

    public void createLoginsFile(Map<String, String> loginToDate) throws IOException {
        StringBuilder content = new StringBuilder("login,last_login\n");
        for (Map.Entry<String, String> entry : loginToDate.entrySet()) {
            content.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }
        Files.write(FILE_PATH_LOGINS, content.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void createBannedFile(List<String> bannedUsers) throws IOException {
        String json = new ObjectMapper().writeValueAsString(bannedUsers);
        Files.write(Path.of(FILE_PATH_BANNED), json.getBytes(StandardCharsets.UTF_8));
    }

    public static String randomUsername() {
        return "user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public static List<String> generateUsernames(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> randomUsername())
                .collect(Collectors.toList());
    }

    public static LocalDate recentDate() {
        int daysAgo = ThreadLocalRandom.current().nextInt(1, 30);
        return LocalDate.now().minusDays(daysAgo);
    }

    public static LocalDate oldDate() {
        int daysAgo = ThreadLocalRandom.current().nextInt(31, 365);
        return LocalDate.now().minusDays(daysAgo);
    }

    public void createInvalidBannedFile() throws IOException {
        Path path = Paths.get("script/data/banned.json");
        Files.write(path, "{invalid}".getBytes());
    }
}