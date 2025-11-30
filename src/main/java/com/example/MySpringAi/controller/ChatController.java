package com.example.MySpringAi.controller;

import com.example.MySpringAi.dto.ChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatClient openaiChatClient;
    private final ChatClient ollamaChatClient;

    /**
     * 當你引入 Spring AI（例如 spring-ai-openai-spring-boot-starter 或其他 provider），Spring 會自動建立一個 ChatClient.Builder Bean，並把必要的設定（例如 model、API Key）都注入。
     */
    @Autowired
    public ChatController(
            @Qualifier("openaiChatClient") ChatClient openaiChatClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient
    ) {
        this.openaiChatClient = openaiChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }

    @PostMapping("/openai/chat")
    public String openaiChat(@RequestBody ChatRequest chatRequest) {
        return openaiChatClient.prompt(chatRequest.message()).call().content();
    }

    @PostMapping("/ollama/chat")
    public String ollamaChat(@RequestBody ChatRequest chatRequest) {
        return ollamaChatClient.prompt(chatRequest.message()).call().content();
    }
}
