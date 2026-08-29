import helpers.DataGenerator;
import helpers.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class CheckActiveUsersTest {

    @Test
    public void shouldIncludeActiveNonBannedUsers() throws Exception {
        List<String> user = DataGenerator.generateUsernames(2);
        String dateUserOne = DateUtils.getYyyy_Dd_MmDate(DataGenerator.recentDate());
        String dateUserTwo = DateUtils.getYyyy_Dd_MmDate(DataGenerator.recentDate());

        new TestScenario()
                .given()
                .withUsersFile(user.get(0), user.get(1))
                .withLoginsFile(Map.of(
                        user.get(0), dateUserOne,
                        user.get(1), dateUserTwo))
                .withBannedFile(List.of())
                .when()
                .executeScript()
                .then()
                .containsActiveUser(user.get(0), dateUserOne)
                .containsActiveUser(user.get(1), dateUserTwo);
    }

    @Test
    public void shouldExcludeBannedUsers() throws Exception {
        List<String> user = DataGenerator.generateUsernames(2);
        String dateUserOne = DateUtils.getYyyy_Dd_MmDate(DataGenerator.recentDate());
        String dateUserTwo = DateUtils.getYyyy_Dd_MmDate(DataGenerator.recentDate());

        new TestScenario()
                .given()
                .withUsersFile(user.get(0), user.get(1))
                .withLoginsFile(Map.of(
                        user.get(0), dateUserOne,
                        user.get(1), dateUserTwo))
                .withBannedFile(List.of(user.get(1)))
                .when()
                .executeScript()
                .then()
                .containsActiveUser(user.get(0), dateUserTwo);
    }

    @Test
    public void shouldExcludeBannedUsersEvenWithRecentLogin() throws Exception {
        List<String> user = DataGenerator.generateUsernames(2);
        String dateUserOne = DateUtils.getYyyy_Dd_MmDate(DataGenerator.recentDate());
        String dateUserTwo = DateUtils.getYyyy_Dd_MmDate(DataGenerator.oldDate());

        new TestScenario()
                .given()
                .withUsersFile(user.get(0), user.get(1))
                .withLoginsFile(Map.of(
                        user.get(0), dateUserOne,
                        user.get(1), dateUserTwo))
                .withBannedFile(List.of(user.get(1)))
                .when()
                .executeScript()
                .then()
                .containsActiveUser(user.get(0), dateUserOne)
                .doesNotContainActiveUser(user.get(1));
    }

    @Test
    public void shouldHandleMissingLoginInLoginsFile() throws Exception {
        List<String> user = DataGenerator.generateUsernames(3);
        String dateUserOne = DateUtils.getYyyy_Dd_MmDate(DataGenerator.recentDate());
        String dateUserTwo = DateUtils.getYyyy_Dd_MmDate(DataGenerator.recentDate());

        new TestScenario()
                .given()
                .withUsersFile(user.get(0), user.get(1), user.get(2))
                .withLoginsFile(Map.of(
                        user.get(0), dateUserOne,
                        user.get(1), dateUserTwo))
                .withBannedFile(List.of())
                .when()
                .executeScript()
                .then()
                .containsActiveUser(user.get(0), dateUserOne)
                .containsActiveUser(user.get(1), dateUserTwo)
                .doesNotContainActiveUser(user.get(2));
    }

    @Test
    public void shouldHandleEmptyUsersFile() throws Exception {
        List<String> user = DataGenerator.generateUsernames(3);
        String dateUserOne = DateUtils.getYyyy_Dd_MmDate(DataGenerator.recentDate());
        String dateUserTwo = DateUtils.getYyyy_Dd_MmDate(DataGenerator.recentDate());

        new TestScenario()
                .given()
                .withUsersFile(user.get(0), user.get(1))
                .withLoginsFile(Map.of(
                        user.get(0), dateUserOne,
                        user.get(1), dateUserTwo))
                .withBannedFile(List.of())
                .when()
                .executeScript("users1.txt", "logins1.csv", "test1.txt")
                .then()
                .doesNotContainActiveUser(user.get(0))
                .doesNotContainActiveUser(user.get(1));
    }
}