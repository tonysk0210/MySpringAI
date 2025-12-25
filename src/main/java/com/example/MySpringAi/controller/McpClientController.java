package com.example.MySpringAi.controller;

import com.example.MySpringAi.payload.GenericChatPayload;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/mcp")

public class McpClientController {

    private final ChatClient chatClient;
    private final ToolCallbackProvider toolCallbackProvider; // 提供「Tool → Callback」對應關係的物件 [mcpToolCallbacks]

    @Autowired
    public McpClientController(@Qualifier("openaiChatClient-jdbcChatMemory") ChatClient chatClient, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chatClient;
        this.toolCallbackProvider = toolCallbackProvider;
    }

    @PostMapping("/mcpchat")
    public ResponseEntity<String> chat(@RequestBody GenericChatPayload payload, @RequestHeader("userName") String userName) {
        String response = chatClient.prompt()
                .advisors(aSpec -> aSpec.param(CONVERSATION_ID, "mcp-" + userName))
                .toolCallbacks(toolCallbackProvider) // LLM 可以使用 這個 ToolCallbackProvider 裡面定義的所有工具」
                .user(payload.message())
                .call().content();
        return ResponseEntity.ok(response);
    }
}
