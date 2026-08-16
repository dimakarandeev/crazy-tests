package helpers;

import java.time.format.DateTimeFormatter;

public enum DateFormat {
    DD_MM_YYYY("dd.MM.yyyy"),
    YYYYMMDD("yyyyMMdd"),
    YYYY_MM_DD("yyyy-MM-dd");

    private final String pattern;

    DateFormat(String pattern) {
        this.pattern = pattern;
    }

    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(pattern);
    }
}