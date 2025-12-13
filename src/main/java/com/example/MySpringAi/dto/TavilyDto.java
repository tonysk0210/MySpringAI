package com.example.MySpringAi.dto;

import java.util.List;

public record TavilyDto(List<Hit> results) {
    public record Hit(String title, String url, String content, Double score) {
    }
}
