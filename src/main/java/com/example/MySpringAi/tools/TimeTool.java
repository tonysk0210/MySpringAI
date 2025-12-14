package com.example.MySpringAi.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Slf4j
@Component
public class TimeTool {

    @Tool(name = "getCurrentLocalTime", description = "get the current local time") // 把一個 Java 方法註冊成「可被 LLM 呼叫的工具（Tool）」
    public String getCurrentLocalTime() {
        log.info("Returning current local time");
        return LocalTime.now().toString();
    }

    @Tool(name = "getCurrentTime", description = "get the current time in a specific time zone")
    public String getCurrentTime(@ToolParam(description = "IANA time zone, e.g. Asia/Taipei, UTC, Europe/London") String timeZone) {
        log.info("Returning current time in time zone: {}", timeZone);
        return LocalTime.now(ZoneId.of(timeZone)).toString();
    }
}
