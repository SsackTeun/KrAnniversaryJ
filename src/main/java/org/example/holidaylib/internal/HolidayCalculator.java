package org.example.holidaylib.internal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;

/**
 * 실 근무일 계산기
 */
public class HolidayCalculator {

    private final HolidayFetcher holidayFetcher;

    public HolidayCalculator(HolidayFetcher holidayFetcher) {
        this.holidayFetcher = holidayFetcher;
    }

    public int calculateWorkingDays(String year, String month, Set<DayOfWeek> offDays) throws Exception {
        YearMonth ym = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
        Set<LocalDate> holidays = holidayFetcher.getHolidays(year, month);

        int workdays = 0;
        int holidayOnOffDay = 0;

        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            LocalDate date = ym.atDay(d);
            DayOfWeek dow = date.getDayOfWeek();
            boolean isOffDay = offDays.contains(dow);
            boolean isHoliday = holidays.contains(date);

            if (!isOffDay) workdays++;
            if (isOffDay && isHoliday) holidayOnOffDay++;
        }

        return workdays - (holidays.size() - holidayOnOffDay);
    }


    public int calculateWorkingDays(String year, String month) throws Exception {
        YearMonth ym = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
        Set<LocalDate> holidays = holidayFetcher.getHolidays(year, month);

        int workdays = 0;
        int weekendHolidayCount = 0;

        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            LocalDate date = ym.atDay(d);

            boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isHoliday = holidays.contains(date);

            if (!isWeekend) workdays++;
            if (isWeekend && isHoliday) weekendHolidayCount++;
        }

        return workdays - (holidays.size() - weekendHolidayCount);
    }
}

