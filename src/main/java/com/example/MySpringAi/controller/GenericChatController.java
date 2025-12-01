package com.example.MySpringAi.controller;

import com.example.MySpringAi.payload.GenericChatPayload;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GenericChatController {

    private final ChatClient openaiChatClient;
    private final ChatClient ollamaChatClient;

    /**
     * 當你引入 Spring AI（例如 spring-ai-openai-spring-boot-starter 或其他 provider），Spring 會自動建立一個 ChatClient.Builder Bean，並把必要的設定（例如 model、API Key）都注入。
     */
    @Autowired
    public GenericChatController(
            @Qualifier("openaiChatClient") ChatClient openaiChatClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient
    ) {
        this.openaiChatClient = openaiChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }


    //openai api
    @PostMapping("/openai/chat")
    public String openaiChat(@RequestBody GenericChatPayload genericChatPayload) {
        return openaiChatClient.prompt(genericChatPayload.message()).call().content();
        // .prompt() 可以接受 1️⃣ 純文字字串 (String), 2️⃣ Message (UserMessage / SystemMessage / AssistantMessage), 3️⃣ Prompt 物件（完整 Prompt, 4️⃣ 包含 Tool / Function 調用的 Prompt
    }

    //ollama api
    @PostMapping("/ollama/chat")
    public String ollamaChat(@RequestBody GenericChatPayload genericChatPayload) {
        return ollamaChatClient.prompt(genericChatPayload.message()).call().content();
    }
}
