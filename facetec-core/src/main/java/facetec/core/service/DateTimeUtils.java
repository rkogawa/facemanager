package facetec.core.service;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by rkogawa on 08/05/19.
 */
public class DateTimeUtils {

    private static DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDateTime getDateTime(String dataISO, String hora) {
        if (StringUtils.isEmpty(dataISO) || StringUtils.isEmpty(hora)) {
            return null;
        }
        return LocalDateTime.of(LocalDate.parse(dataISO, ISO_FORMAT), LocalTime.parse(hora, DateTimeFormatter.ISO_LOCAL_TIME));
    }

    public static String convertISODate(LocalDateTime dateTime) {
        return dateTime != null ? DateTimeFormatter.ISO_LOCAL_DATE.format(dateTime) : null;
    }

    public static String convertTime(LocalDateTime dateTime) {
        return dateTime != null ? DateTimeFormatter.ISO_LOCAL_TIME.format(dateTime) : null;
    }
}
