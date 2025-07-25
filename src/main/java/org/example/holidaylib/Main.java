package org.example.holidaylib;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {

            // apikey
            String apiKey = "";
            HolidayLib lib = new HolidayLib(apiKey);

            // 실 근무일 수
            for (int i = 1; i <= 12; i++) {
                int workingDays = lib.calculateWorkingDays("2025", String.valueOf(i));
                System.out.println(String.format("%d월 실 근무일 수: %d", i, workingDays));
            }

            // 공휴일 확인
            System.out.println("==== [2025년 10월의 공휴일 목록] ====");
            lib.getHolidays("2025", "10").forEach(System.out::println);

            System.out.println("\n==== [2025년 전체 공휴일 목록] ====");
            lib.getHolidays("2025").forEach(System.out::println);


            // 주 5일제 (토, 일 휴무)
            Set<DayOfWeek> offDays_5 = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
            int workingDays5 = lib.calculateWorkingDays("2025", "10", offDays_5);
            System.out.printf("[주 5일제] 2025년 10월 실 근무일 수: %d일%n", workingDays5);

            // 주 6일제 (일요일만 휴무)
            Set<DayOfWeek> offDays_6 = EnumSet.of(DayOfWeek.SUNDAY);
            int workingDays6 = lib.calculateWorkingDays("2025", "10", offDays_6);
            System.out.printf("[주 6일제] 2025년 10월 실 근무일 수: %d일%n", workingDays6);

            // 주 4일제 (금, 토, 일 휴무)
            Set<DayOfWeek> offDays_4 = EnumSet.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
            int workingDays4 = lib.calculateWorkingDays("2025", "10", offDays_4);
            System.out.printf("[주 4일제] 2025년 10월 실 근무일 수: %d일%n", workingDays4);

        } catch (Exception e) {
            System.err.println("예외 발생: " + e.getMessage());
            e.printStackTrace(); // 여기가 중요!
        }
    }

}

