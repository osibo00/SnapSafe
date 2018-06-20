package productions.darthplagueis.contentvault.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CurrentDateUtil {

    public static String getDateString() {
        long currentTimeMillis = System.currentTimeMillis();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date(currentTimeMillis);
        return dateFormat.format(date);
    }

    public static long getTimeStamp() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
