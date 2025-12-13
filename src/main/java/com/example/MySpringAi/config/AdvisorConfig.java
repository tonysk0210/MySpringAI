package com.example.MySpringAi.config;

import com.example.MySpringAi.component.rag.TavilyWebSearchDocumentRetriever;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class AdvisorConfig {

    // 「專門負責 PDF RAG」的 Advisor Bean
    @Bean
    @Primary
    public RetrievalAugmentationAdvisor pdfRetrievalAugmentationAdvisor(@Qualifier("pdfVectorStore") VectorStore vectorStore) {
        // 建立一個 RetrievalAugmentationAdvisor，內部用 VectorStore 做檢索
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                                // 指定要用的 collection（pdf-collection）
                                .vectorStore(vectorStore)
                                // 回傳前 3 筆相似片段
                                .topK(3)
                                // 相似度過低則忽略
                                .similarityThreshold(0.5)
                                .build())
                .build();
    }

    @Bean
    @Qualifier("TrvilyRAAdvisor")
    public RetrievalAugmentationAdvisor trvilyRetrievalAugmentationAdvisor() {
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(
                        TavilyWebSearchDocumentRetriever.builder()
                                .restClientBuilder(RestClient.builder()) // RestClient.builder() 是「Spring Framework 提供的 API」
                                .resultLimit(5)
                                .build()
                ).build();
    }
}
