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
        // create a chat client with the OpenAiChatModel
        return ChatClient.create(openAiChatModel);
    }

    @Bean("ollamaChatClient")
    public ChatClient ollmaChatClient(OllamaChatModel ollamaChatModel) {
        //another way to create a chat client with the OllamaChatModel
        return ChatClient.builder(ollamaChatModel).build();
    }
}
