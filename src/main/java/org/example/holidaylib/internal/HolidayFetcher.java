package org.example.holidaylib;

import org.example.holidaylib.config.HolidayConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class HolidayFetcher {

    private final String apiKey;

    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter OUTPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public HolidayFetcher(HolidayConfig config) {
        this.apiKey = config.getApiKey();
    }

    public Set<String> getHolidays(String year) throws UnsupportedEncodingException, JsonProcessingException {
        Set<String> holidays = new HashSet<>();
        for (int month = 1; month <= 12; month++) {
            holidays.addAll(fetchHolidays(year, String.format("%02d", month)));
        }
        return holidays;
    }

    public Set<String> getHolidays(String year, String month) throws UnsupportedEncodingException, JsonProcessingException {
        return fetchHolidays(year, month);
    }

    public Set<String> fetchHolidays(String year, String month) throws UnsupportedEncodingException {
        String encodedApiKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
        URI uri = UriComponentsBuilder.fromUriString("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
                .queryParam("serviceKey", encodedApiKey)
                .queryParam("solYear", year)
                .queryParam("solMonth", month)
                .queryParam("numOfRows", 100)
                .build(true)
                .toUri();

        RestDeInfoDTO response = WebClient.create()
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(RestDeInfoDTO.class)
                .block();

        Object items = response.getResponse().getBody().getItems();
        Set<String> holidaySet = new HashSet<>();
        ObjectMapper objectMapper = new ObjectMapper();

        if (items instanceof LinkedHashMap) {
            Object itemData = objectMapper.convertValue(((LinkedHashMap<?, ?>) items).get("item"), new TypeReference<>() {});
            if (itemData instanceof ArrayList) {
                ((ArrayList<?>) itemData).forEach(x -> {
                    RestDeInfoDTO.Item i = objectMapper.convertValue(x, RestDeInfoDTO.Item.class);
                    LocalDate date = LocalDate.parse(i.getLocdate(), API_DATE_FORMATTER);
                    holidaySet.add(date.format(OUTPUT_DATE_FORMATTER));
                });
            } else if (itemData instanceof LinkedHashMap) {
                ((LinkedHashMap<?, ?>) itemData).forEach((x, y) -> {
                    if (x.equals("locdate")) {
                        LocalDate date = LocalDate.parse(y.toString(), API_DATE_FORMATTER);
                        holidaySet.add(date.format(OUTPUT_DATE_FORMATTER));
                    }
                });
            }
        }
        return holidaySet;
    }
}
