package com.example.MySpringAi.config;

import com.example.MySpringAi.payload.GenericChatPayload;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api")
public class RagController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/promptTemplate/RagPromptTemplate.st")
    Resource ragPromptTemplate;

    @Autowired
    public RagController(@Qualifier("openaiChatClient-jdbcChatMemory") ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @PostMapping("/rag")
    public String openaiChat(@RequestBody GenericChatPayload genericChatPayload, @RequestHeader("userName") String userName) {
        // 一次「向量搜尋條件」的封裝物件
        SearchRequest searchRequest = SearchRequest.builder()
                .query(genericChatPayload.message()) // 用這一句話去找「語意上最接近」的 Documents
                .topK(5)                              // 只要最相似的 前 5 筆 Document
                .similarityThreshold(.5)           // 搜尋的相似度門檻
                .build();

        List<Document> listOfSimilarDocuments = vectorStore.similaritySearch(searchRequest); // 從向量資料庫撈出最相關的知識片段
        String similarContext = listOfSimilarDocuments.stream().map(Document::getText).collect(Collectors.joining(",\n")); // 將所有 Document 轉成 String

        return chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, "rag-" + userName))
                .system(systemSpec -> systemSpec.text(ragPromptTemplate).param("documents", similarContext)) // 將知識片段加到 System Prompt
                .user(genericChatPayload.message()) // 將用戶的訊息加到 User Prompt
                .call().content();
    }
}
