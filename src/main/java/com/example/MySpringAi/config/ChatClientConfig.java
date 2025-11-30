package com.example.MySpringAi.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAiChatModel 和 OllamaChatModel 是 Spring 自動建立的 Bean 在 pom.xml 引入
 */
@Configuration
public class ChatClientConfig {

    @Bean("openaiChatClient")
    public ChatClient openaiChatClient(OpenAiChatModel openAiChatModel) {

        return ChatClient.builder(openAiChatModel)
                .defaultSystem("回答時請使用清楚、易理解且專業的繁體中文。")
                .build();
    }

    @Bean("ollamaChatClient")
    public ChatClient ollmaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("回答時請使用清楚、易理解且專業的繁體中文。")
                .build();
    }
}
