package com.example.MySpringAi.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
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
    public ChatClient openaiChatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory) {
        //ChatMemory 已經是一個 Bean（由 MessageWindowChatMemory 建立），並且會被成功注入

        ChatOptions chatOptions = ChatOptions.builder().temperature(0.5).maxTokens(500).build();  // 設定模型用的「參數」：

        Advisor inMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build(); // 建立 MessageChatMemoryAdvisor，也就是「會話記憶攔截器」。

        return ChatClient.builder(openAiChatModel)
                .defaultOptions(chatOptions)
                .defaultAdvisors(new SimpleLoggerAdvisor(), inMemoryAdvisor) // 加入預設 Advisor（攔截器）
                .defaultSystem("回答時請使用清楚、易理解且專業的繁體中文。")
                .build();
    }

    @Bean("ollamaChatClient")
    public ChatClient ollmaChatClient(OllamaChatModel ollamaChatModel, ChatMemory chatMemory) {
        ChatOptions chatOptions = ChatOptions.builder().temperature(0.5).maxTokens(500).build();
        Advisor inMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return ChatClient.builder(ollamaChatModel)
                .defaultOptions(chatOptions)
                .defaultAdvisors(new SimpleLoggerAdvisor(), inMemoryAdvisor)
                .defaultSystem("回答時請使用清楚、易理解且專業的繁體中文。")
                .build();
    }
}
