package org.example.holidaylib;

import java.io.UnsupportedEncodingException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.holidaylib.internal.HolidayCalculator;
import org.example.holidaylib.internal.HolidayConfig;
import org.example.holidaylib.internal.HolidayFetcher;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 공휴일 계산 라이브러리 진입점
 */
public class HolidayLib {

    private final HolidayFetcher holidayFetcher;
    private final HolidayCalculator holidayCalculator;

    // API KEY 주입 및 의존성
    public HolidayLib(String apiKey) {
        // APIKEY
        HolidayConfig config = new HolidayConfig(apiKey);

        // 휴일 가져오는 API
        holidayFetcher = new HolidayFetcher(config);

        // 휴일 계산 관련
        holidayCalculator = new HolidayCalculator(holidayFetcher);
    }

    // 실 근무 일 수 계산 : 주 n 일
    public int calculateWorkingDays(String year, String month, Set<DayOfWeek> offDays) throws Exception {
        return holidayCalculator.calculateWorkingDays(year, month, offDays);
    }

    // 실 근무 일 수 계산 : 토요일, 일요일, 대체공휴일 제외 (주 5일)
    public int calculateWorkingDays(String year, String month) throws Exception {
        return holidayCalculator.calculateWorkingDays(year, month);
    }

    // 해당 년도의 휴일을 2025.01.01 같은 형식으로 반환
    public Set<LocalDate> getHolidays(String year) throws UnsupportedEncodingException, JsonProcessingException {
        return holidayFetcher.getHolidays(year).stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    // 해당 년도와 월을 조건으로 휴일을 2025.01.01 형식으로 반환
    public Set<LocalDate> getHolidays(String year, String month) throws Exception {
        return holidayFetcher.getHolidays(year, month).stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
