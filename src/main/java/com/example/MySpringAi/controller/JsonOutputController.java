package com.example.MySpringAi.controller;

import com.example.MySpringAi.dto.CountryCitiesDto;
import com.example.MySpringAi.payload.JsonOutputPayload;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JsonOutputController {

    private final ChatClient openaiChatClient;
    private final ChatClient ollamaChatClient;

    @Autowired
    public JsonOutputController(
            @Qualifier("openaiChatClient") ChatClient openaiChatClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient
    ) {
        this.openaiChatClient = openaiChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }

    // convert llm text to JSON DTO openai
    @PostMapping("/openai/generateJsonDto")
    public ResponseEntity<CountryCitiesDto> openaiGenerateJsonDto(@RequestBody JsonOutputPayload jsonOutputPayload) {
        CountryCitiesDto dto = openaiChatClient.prompt()
                .user(jsonOutputPayload.message())
                .call()
                .entity(CountryCitiesDto.class); // content to JSON
        return ResponseEntity.ok(dto);
    }

    // convert llm text to JSON DTO ollama
    @PostMapping("/ollama/generateJsonDto")
    public ResponseEntity<CountryCitiesDto> ollamaGenerateJsonDto(@RequestBody JsonOutputPayload jsonOutputPayload) {
        CountryCitiesDto dto = ollamaChatClient.prompt()
                .user(jsonOutputPayload.message())
                .call()
                .entity(CountryCitiesDto.class); // content to JSON
        return ResponseEntity.ok(dto);
    }


}
