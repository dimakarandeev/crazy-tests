package helpers;

import java.time.LocalDate;

public class DateUtils {

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static String getYyyy_Dd_MmDate(LocalDate date) {
        return date.format(DateFormat.YYYY_MM_DD.getFormatter());
    }
}
