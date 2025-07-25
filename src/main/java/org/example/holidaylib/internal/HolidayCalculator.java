package org.example.holidaylib;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;

/**
 * 실 근무일 계산기
 */
class HolidayCalculator {

    private final HolidayFetcher holidayFetcher;

    public HolidayCalculator(HolidayFetcher holidayFetcher) {
        this.holidayFetcher = holidayFetcher;
    }

    public int calculateWorkingDays(String year, String month) throws Exception {
        YearMonth ym = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
        Set<String> holidays = holidayFetcher.getHolidays(year, month);

        int workdays = 0;
        int weekendHolidayCount = 0;

        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            LocalDate date = ym.atDay(d);
            String formattedDate = date.format(HolidayFetcher.OUTPUT_DATE_FORMATTER);

            boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isHoliday = holidays.contains(formattedDate);

            if (!isWeekend) workdays++;
            if (isWeekend && isHoliday) weekendHolidayCount++;
        }

        return workdays - (holidays.size() - weekendHolidayCount);
    }
}

