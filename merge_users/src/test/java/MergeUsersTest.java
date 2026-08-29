import helpers.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class MergeUsersTest {

    @Test
    public void shouldMergeAllUsersCorrectly() throws Exception {
        List<String> user = DataGenerator.generateUsernames(3);

        String firstUserName = DataGenerator.generateFullName();
        String secondUserName = DataGenerator.generateFullName();
        String thirdUserName = DataGenerator.generateFullName();

        String firstUserEmail = DataGenerator.generateEmail(user.get(0));
        String secondUserEmail = DataGenerator.generateEmail(user.get(1));
        String thirdUserEmail = DataGenerator.generateEmail(user.get(2));

        new TestScenario()
                .given()
                .withUsersFile(user.get(0), user.get(1), user.get(2))
                .withUsersJsonFile(Map.of(
                        user.get(0), firstUserName,
                        user.get(1), secondUserName,
                        user.get(2), thirdUserName
                ))
                .withUsersCsvFile(Map.of(
                        user.get(0), firstUserEmail,
                        user.get(1), secondUserEmail,
                        user.get(2), thirdUserEmail
                ))
                .when()
                .executeScript()
                .then()
                .containsFullUser(user.get(0), firstUserName, firstUserEmail)
                .containsFullUser(user.get(1), secondUserName, secondUserEmail)
                .containsFullUser(user.get(2), thirdUserName, thirdUserEmail);
    }

    @Test
    public void shouldSkipUserMissingInJson() throws Exception {
        List<String> user = DataGenerator.generateUsernames(2);

        String firstUserName = DataGenerator.generateFullName();

        String firstUserEmail = DataGenerator.generateEmail(user.get(0));
        String secondUserEmail = DataGenerator.generateEmail(user.get(1));

        new TestScenario()
                .given()
                .withUsersFile(user.get(0), user.get(1))
                .withUsersJsonFile(Map.of(
                        user.get(0), firstUserName
                ))
                .withUsersCsvFile(Map.of(
                        user.get(0), firstUserEmail,
                        user.get(1), secondUserEmail
                ))
                .when()
                .executeScript()
                .then()
                .containsFullUser(user.get(0), firstUserName, firstUserEmail)
                .doesNotContainFullUser(user.get(1));
    }

    @Test
    public void shouldSkipUserMissingInCsv() throws Exception {
        List<String> user = DataGenerator.generateUsernames(3);

        String firstUserName = DataGenerator.generateFullName();
        String secondUserName = DataGenerator.generateFullName();

        String firstUserEmail = DataGenerator.generateEmail(user.get(0));

        new TestScenario()
                .given()
                .withUsersFile(user.get(0), user.get(1))
                .withUsersJsonFile(Map.of(
                        user.get(0), firstUserName,
                        user.get(1), secondUserName
                ))
                .withUsersCsvFile(Map.of(
                        user.get(0), firstUserEmail
                ))
                .when()
                .executeScript()
                .then()
                .containsFullUser(user.get(0), firstUserName, firstUserEmail)
                .doesNotContainFullUser(user.get(1));
    }

    @Test
    public void shouldHandleEmptyUsersFile() throws Exception {
        List<String> user = DataGenerator.generateUsernames(3);
        String firstUserName = DataGenerator.generateFullName();
        String firstUserEmail = DataGenerator.generateEmail(user.get(0));

        new TestScenario()
                .given()
                .withUsersFile()
                .withUsersJsonFile(Map.of(user.get(0), firstUserName))
                .withUsersCsvFile(Map.of(user.get(0), firstUserEmail))
                .when()
                .executeScript()
                .then()
                .doesNotContainFullUser(user.get(0));
    }

    @Test
    public void shouldNotDuplicateUsers() throws Exception {
        List<String> user = DataGenerator.generateUsernames(3);

        String firstUserName = DataGenerator.generateFullName();
        String secondUserName = DataGenerator.generateFullName();

        String firstUserEmail = DataGenerator.generateEmail(user.get(0));
        String secondUserEmail = DataGenerator.generateEmail(user.get(1));

        new TestScenario()
                .given()
                .withUsersFile(user.get(0), user.get(0), user.get(1))
                .withUsersJsonFile(Map.of(
                        user.get(0), firstUserName,
                        user.get(1), secondUserName
                ))
                .withUsersCsvFile(Map.of(
                        user.get(0), firstUserEmail,
                        user.get(1), secondUserEmail
                ))
                .when()
                .executeScript()
                .then()
                .containsFullUser(user.get(0), firstUserName, firstUserEmail)
                .containsFullUser(user.get(1), secondUserName, secondUserEmail);
    }
}