package net.extrategy.bernardo;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BernardoWidgetProviderUnitTest {

    @Test
    public void onBusinessHour_shouldBeTrue() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        assertEquals(BernardoWidgetProvider.isBusinessHoursForTesting(calendar), false);

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        assertEquals(BernardoWidgetProvider.isBusinessHoursForTesting(calendar), true);

        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        assertEquals(BernardoWidgetProvider.isBusinessHoursForTesting(calendar), true);

        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        assertEquals(BernardoWidgetProvider.isBusinessHoursForTesting(calendar), false);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        assertEquals(BernardoWidgetProvider.isBusinessHoursForTesting(calendar), false);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        assertEquals(BernardoWidgetProvider.isBusinessHoursForTesting(calendar), false);

    }
}