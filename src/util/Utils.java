package util;

import java.time.LocalDate;
import java.util.UUID;

public class Utils {
    public static String generateGUID() {
	UUID uuid = UUID.randomUUID();
	return uuid.toString();
    }

    public static boolean isBetween(LocalDate fromDate, LocalDate toDate, LocalDate date) {
	return date.isBefore(toDate) && date.isAfter(fromDate);
    }
}
