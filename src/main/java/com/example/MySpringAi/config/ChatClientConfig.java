package com.example.MySpringAi.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
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
        ChatOptions chatOptions = ChatOptions.builder().temperature(0.5).maxTokens(500).build(); // 設定模型參數

        return ChatClient.builder(openAiChatModel)
                .defaultOptions(chatOptions)
                .defaultAdvisors(new SimpleLoggerAdvisor()) // 打印日志
                .defaultSystem("回答時請使用清楚、易理解且專業的繁體中文。")
                .build();
    }

    @Bean("ollamaChatClient")
    public ChatClient ollmaChatClient(OllamaChatModel ollamaChatModel) {
        ChatOptions chatOptions = ChatOptions.builder().temperature(0.5).maxTokens(500).build(); // 設定模型參數

        return ChatClient.builder(ollamaChatModel)
                .defaultOptions(chatOptions)
                .defaultAdvisors(new SimpleLoggerAdvisor()) // 打印日志
                .defaultSystem("回答時請使用清楚、易理解且專業的繁體中文。")
                .build();
    }
}
