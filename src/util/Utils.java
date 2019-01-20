package util;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Util functions used everywhere
 * 
 * @author fkg
 *
 */
public class Utils {
	/**
	 * Generate a random GUID
	 * 
	 * @return guid
	 */
	public static String generateGUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * Check if date is between two dates
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param date
	 * @return isBetween
	 */
	public static boolean isBetween(LocalDate fromDate, LocalDate toDate, LocalDate date) {
		// inverse and change after/before is the same and checks if their are equal
		return !date.isAfter(toDate) && !date.isBefore(fromDate);
	}
}
