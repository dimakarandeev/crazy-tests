import org.junit.jupiter.api.Test;

import java.util.Map;

public class MergeUsersTest {

    @Test
    public void shouldMergeAllUsersCorrectly() throws Exception {
        new TestScenario()
                .given()
                .withUsersFile("alice", "bob", "carol")
                .withUsersJsonFile(Map.of(
                        "alice", "Alice Smith",
                        "bob", "Bob Johnson",
                        "carol", "Carol Lee"
                ))
                .withUsersCsvFile(Map.of(
                        "alice", "alice@example.com",
                        "bob", "bob@example.com",
                        "carol", "carol@example.com"
                ))
                .when()
                .executeScript()
                .then()
                .containsFullUser("alice", "Alice Smith", "alice@example.com")
                .containsFullUser("bob", "Bob Johnson", "bob@example.com")
                .containsFullUser("carol", "Carol Lee", "carol@example.com");
    }

    @Test
    public void shouldSkipUserMissingInJson() throws Exception {
        new TestScenario()
                .given()
                .withUsersFile("alice", "bob")
                .withUsersJsonFile(Map.of(
                        "alice", "Alice Smith"
                ))
                .withUsersCsvFile(Map.of(
                        "alice", "alice@example.com",
                        "bob", "bob@example.com"
                ))
                .when()
                .executeScript()
                .then()
                .containsFullUser("alice", "Alice Smith", "alice@example.com")
                .doesNotContainFullUser("bob");
    }

    @Test
    public void shouldSkipUserMissingInCsv() throws Exception {
        new TestScenario()
                .given()
                .withUsersFile("alice", "bob")
                .withUsersJsonFile(Map.of(
                        "alice", "Alice Smith",
                        "bob", "Bob Johnson"
                ))
                .withUsersCsvFile(Map.of(
                        "alice", "alice@example.com"
                ))
                .when()
                .executeScript()
                .then()
                .containsFullUser("alice", "Alice Smith", "alice@example.com")
                .doesNotContainFullUser("bob");
    }

    @Test
    public void shouldHandleEmptyUsersFile() throws Exception {
        new TestScenario()
                .given()
                .withUsersFile()
                .withUsersJsonFile(Map.of("alice", "Alice Smith"))
                .withUsersCsvFile(Map.of("alice", "alice@example.com"))
                .when()
                .executeScript()
                .then()
                .doesNotContainFullUser("alice");
    }

    @Test
    public void shouldNotDuplicateUsers() throws Exception {
        new TestScenario()
                .given()
                .withUsersFile("alice", "alice", "bob")
                .withUsersJsonFile(Map.of(
                        "alice", "Alice Smith",
                        "bob", "Bob Johnson"
                ))
                .withUsersCsvFile(Map.of(
                        "alice", "alice@example.com",
                        "bob", "bob@example.com"
                ))
                .when()
                .executeScript()
                .then()
                .containsFullUser("alice", "Alice Smith", "alice@example.com")
                .containsFullUser("bob", "Bob Johnson", "bob@example.com");
    }
}
