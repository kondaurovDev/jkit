package jkit.core.ext;

import lombok.var;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class TimeExtTest {

    @Test
    void getDifferenceInSeconds() throws Exception {

        var compared1 =
            TimeExt.parse("10/01/20 10:00:00");

        var compared2 =
            TimeExt.parse("10/01/20 15:05:00");

        long diff = TimeExt.getDifferenceInSeconds(compared2.get(), compared1.get());

        assertTrue(diff > 0);
    }

    @Test
    void tz() {

        TimeExt.setUtc();
        Calendar calendar = Calendar.getInstance();
        var dt1 = DateTime.now();
        var dt = calendar.getTime();
        assertEquals(calendar.getTimeZone(), TimeZone.getTimeZone("UTC"));

    }

    @Test
    void ago() {

        var actual = TimeExt.getAgoSince(
            TimeExt.parse("20/01/20 00:00:03").get().toDateTime(),
            TimeExt.parse("10/01/20 00:00:00").get().toDateTime()
        );

        assertEquals(actual, "10 days");

    }

    @Test
    void fromTimestamp() {

        TimeExt.setUtc();
        var actual = TimeExt.parse(TimeExt.getCurrentTimestamp());

        assertTrue(actual.isRight());

    }

    @Test
    void getNextWeekDay() {

        var current = TimeExt.parse("15/09/20 00:00:03").get();

        assertEquals(
            TimeExt.parse("18/09/20 00:00:03").get(),
            TimeExt.getNextWeekDay(
                current,
                DayOfWeek.FRIDAY
            )
        );

        assertEquals(
            TimeExt.parse("21/09/20 00:00:03").get(),
            TimeExt.getNextWeekDay(
                current,
                DayOfWeek.MONDAY
            )
        );

    }

    @Test
    void getFirstWeekDayOfNextMonth() {

        assertEquals(
            TimeExt.parse("03/10/20 00:00:03").get(),
            TimeExt.getFirstWeekDayOfNextMonth(
                TimeExt.parse("15/09/20 00:00:03").get(),
                DayOfWeek.SATURDAY
            )
        );

    }

}