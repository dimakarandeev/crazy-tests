import org.junit.jupiter.api.Test;

public class CalculateAveragesPerformanceTest {

    @Test
    public void processing10kRowsTakesLessThan1Second() {
        new TestScenario()
                .given()
                .generateRecords(10000)
                .when()
                .executeScriptWithTiming()
                .then()
                .executionTimeLessThan(1000);
    }

    @Test
    public void processing100kRowsTakesLessThan5Seconds() {
        new TestScenario()
                .given()
                .generateRecords(100000)
                .when()
                .executeScriptWithTiming()
                .then()
                .executionTimeLessThan(5000);
    }

    @Test
    public void processing1MillionRowsTakesLessThan20Seconds() {
        new TestScenario()
                .given()
                .generateRecords(1000000)
                .when()
                .executeScriptWithTiming()
                .then()
                .executionTimeLessThan(20000);
    }
}