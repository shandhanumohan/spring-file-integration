package com.sample.spring.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class FileChannelConfig {
    @Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "100"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("./test-files"));
        source.setFilter(new SimplePatternFileListFilter("*.txt"));
        return source;
    }

    @Bean
    public FileToStringTransformer fileToStringTransformer() {
        return new FileToStringTransformer();
    }

    @Bean
    public IntegrationFlow processFileFlow() {
        //10 threads are created for concurrent execution
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        return IntegrationFlows
                .from("fileInputChannel")
                .transform(fileToStringTransformer())
                .channel(MessageChannels.executor(executorService))
                .handle("fileProcessor", "process").get();
    }


}
