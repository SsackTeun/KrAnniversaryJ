package org.example.holidaylib.config;

import lombok.Getter;

/**
 * 공휴일 API를 위한 설정 클래스
 */
@Getter
public class HolidayConfig {
    private final String apiKey;

    public HolidayConfig(String apiKey) {
        this.apiKey = apiKey;
    }

}

