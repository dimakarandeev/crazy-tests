import helpers.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class CheckActiveUsersTest {

    String dateAlice = DateUtils.getYyyy_Dd_MmDate(DateUtils.today().minusDays(10));
    String dateBob = DateUtils.getYyyy_Dd_MmDate(DateUtils.today().minusDays(11));

    @Test
    public void shouldIncludeActiveNonBannedUsers() throws Exception {
        new TestScenario()
                .given()
                .withUsersFile("alice", "bob")
                .withLoginsFile(Map.of(
                        "alice", dateAlice,
                        "bob", dateBob))
                .withBannedFile(List.of())
                .when()
                .executeScript()
                .then()
                .containsActiveUser("alice", dateAlice)
                .containsActiveUser("bob", dateBob);
    }

    @Test
    public void shouldExcludeBannedUsers() throws Exception {
        new TestScenario()
                .given()
                .withUsersFile("alice", "bob")
                .withLoginsFile(Map.of(
                        "alice", dateAlice,
                        "bob", dateBob))
                .withBannedFile(List.of("bob"))
                .when()
                .executeScript()
                .then()
                .containsActiveUser("alice", dateAlice);
    }

    @Test
    public void shouldExcludeBannedUsersEvenWithRecentLogin() throws Exception {
        String dateBob = DateUtils.getYyyy_Dd_MmDate(DateUtils.today().minusDays(31));

        new TestScenario()
                .given()
                .withUsersFile("alice", "bob")
                .withLoginsFile(Map.of(
                        "alice", dateAlice,
                        "bob", dateBob))
                .withBannedFile(List.of("bob"))
                .when()
                .executeScript()
                .then()
                .containsActiveUser("alice", dateAlice)
                .doesNotContainActiveUser("bob");
    }

    @Test
    public void shouldHandleMissingLoginInLoginsFile() throws Exception {
        new TestScenario()
                .given()
                .withUsersFile("alice", "bob", "carol")
                .withLoginsFile(Map.of(
                        "alice", dateAlice,
                        "bob", dateBob))
                .withBannedFile(List.of())
                .when()
                .executeScript()
                .then()
                .containsActiveUser("alice", dateAlice)
                .containsActiveUser("bob", dateBob)
                .doesNotContainActiveUser("carol");
    }

    @Test
    public void shouldHandleEmptyUsersFile() throws Exception {
        new TestScenario()
                .given()
                .withUsersFile("alice", "bob")
                .withLoginsFile(Map.of(
                        "alice", dateAlice,
                        "bob", dateBob))
                .withBannedFile(List.of())
                .when()
                .executeScript("users1.txt", "logins1.csv", "test1.txt")
                .then()
                .doesNotContainActiveUser("alice")
                .doesNotContainActiveUser("bob");
    }
}