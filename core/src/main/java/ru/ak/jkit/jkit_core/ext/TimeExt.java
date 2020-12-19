package ru.ak.jkit.jkit_core.ext;

import io.vavr.Function1;
import io.vavr.control.Try;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.TimeZone;

public interface TimeExt {

    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd/MM/YY HH:mm:ss");
    DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm:ss");

    static void setUtc() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    static String getString(LocalDateTime dt) {
        return dt.toString(dateFormat);
    }

    static long diffInSecWithCurrent(LocalDateTime ts) {
        return getDifferenceInSeconds(getCurrent(), ts);
    }

    static Try<LocalDateTime> parse(String dt) {
        return TryExt.get(
            () -> LocalDateTime.parse(dt, dateFormat),
            "date time from string"
        );
    }

    static Try<LocalDateTime> parse(Long dt) {
        return parse(new Timestamp(dt));
    }

    static Try<LocalDateTime> parse(Object dt) {
        return TryExt.get(
            () -> new LocalDateTime(dt),
            "date time from object"
        );
    }

    static LocalDateTime getCurrent(Function1<LocalDateTime, LocalDateTime> apply) {
        return apply.apply(LocalDateTime.now());
    }

    static LocalDateTime getCurrent() {
        return getCurrent(Function1.identity());
    }

    static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    static Long getSeconds(LocalDateTime ts) {
        return ts.toDateTime().getMillis() / 1000;
    }

    static Long getDifferenceInSeconds(LocalDateTime a, LocalDateTime b) {
        return getSeconds(a) - getSeconds(b);
    }

    static String getAgoSince(DateTime ts) {
        return getAgoSince(Instant.now().toDateTime(), ts);
    }

    static Timestamp getTimestamp(DateTime dt) {
        return new Timestamp(dt.toInstant().getMillis());
    }
    static Timestamp getTimestamp(Instant instant) {
        return new Timestamp(instant.getMillis());
    }

    static Try<Timestamp> getTimestamp(String s) {
        return TimeExt.parse(s).map(dt -> TimeExt.getTimestamp(dt.toDateTime()));
    }

    static String getAgoSince(DateTime first, DateTime last) {
        int seconds = Seconds.secondsBetween(last, first).getSeconds();

        int interval = seconds / 31536000;

        if (interval > 1) {
            return Math.round(interval) + " years";
        }

        interval = seconds / 2592000;
        if (interval > 1) {
            return Math.round(interval) + " months";
        }

        interval = seconds / 86400;
        if (interval > 1) {
            return Math.round(interval) + " days";
        }

        interval = seconds / 3600;
        if (interval > 1) {
            return Math.round(interval) + " hours";
        }

        interval = seconds / 60;
        if (interval > 1) {
            return Math.round(interval) + " minutes";
        }

        return Math.round(seconds) + " seconds";
    }

    static LocalDateTime getNextWeekDay(
        LocalDateTime current,
        DayOfWeek weekDay
    ) {
        int currentDay = current.getDayOfWeek();
        int day = weekDay.getValue();

        if (day <= current.getDayOfWeek()) {
            day += 7;
        }

        return current.plusDays(day - currentDay);
    }

    static LocalDateTime getFirstWeekDayOfNextMonth(
        LocalDateTime current,
        DayOfWeek weekDay
    ) {
        LocalDateTime firstDay = current.dayOfMonth().withMinimumValue().plusMonths(1);
        if (firstDay.getDayOfWeek() == weekDay.getValue()) {
            return firstDay;
        }
        return getNextWeekDay(
            firstDay,
            weekDay
        );
    }

}
