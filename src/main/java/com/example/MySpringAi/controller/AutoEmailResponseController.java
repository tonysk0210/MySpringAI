package com.example.MySpringAi.controller;

import com.example.MySpringAi.payload.AutoEmailResponsePayload;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AutoEmailResponseController {

    private final ChatClient openaiChatClient;
    private final ChatClient ollamaChatClient;

    @Autowired
    public AutoEmailResponseController(
            @Qualifier("openaiChatClient") ChatClient openaiChatClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient
    ) {
        this.openaiChatClient = openaiChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }

    @Value("classpath:promptTemplate/AutoEmailResponsePromptTemplate.st")
    private Resource emailResponsePromptTemplateText;

    /**
     * 這個方法會使用 Spring AI 的 PromptTemplate 來生成 Prompt，然後使用 ChatClient 來生成對應的 Response
     */
    //openai auto-generate email response given customer name & customer concern
    @PostMapping("/openai/emailResponse")
    public String openaiEmailResponse(@RequestBody AutoEmailResponsePayload autoEmailResponsePayload) {
        return openaiChatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(emailResponsePromptTemplateText)
                        .param("customerName", autoEmailResponsePayload.customerName())
                        .param("customerMessage", autoEmailResponsePayload.customerMessage()))
                .call().content();
    }

    //ollama auto-generate email response given customer name & customer concern
    @PostMapping("/ollama/emailResponse")
    public String ollamaEmailResponse(@RequestBody AutoEmailResponsePayload autoEmailResponsePayload) {
        PromptTemplate promptTemplate = new PromptTemplate(emailResponsePromptTemplateText);
        Prompt prompt = promptTemplate.create(Map.of(
                "customerName", autoEmailResponsePayload.customerName(),
                "customerMessage", autoEmailResponsePayload.customerMessage()
        ));
        return ollamaChatClient.prompt(prompt).call().content();
    }
}
