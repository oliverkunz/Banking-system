package util;

import java.time.LocalDate;
import java.util.UUID;

public class Utils {
    public static String generateGUID() {
	UUID uuid = UUID.randomUUID();
	return uuid.toString();
    }

    public static boolean isBetween(LocalDate fromDate, LocalDate toDate, LocalDate date) {
	// inverse and change after/before is the same and checks if their are equal
	return !date.isAfter(toDate) && !date.isBefore(fromDate);
    }
}
