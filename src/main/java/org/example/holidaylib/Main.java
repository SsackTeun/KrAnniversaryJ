package org.example;

import org.example.holidaylib.HolidayLib;

public class Main {
    public static void main(String[] args) throws Exception {
        String apiKey = "DVcn3lMambqbUG2eCIroEmHMdcjD8IADQy/2+Q1nr7S23NymKJNSduAKjSFMnRKavNuUkGoD0ZyOgUC+B/jQ4g==";
        HolidayLib lib = new HolidayLib(apiKey);

        int workingDays = lib.calculateWorkingDays("2025", "07");
        System.out.println("2025년 7월 실 근무일 수: " + workingDays);
    }
}

