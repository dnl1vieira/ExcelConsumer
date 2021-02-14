package com.vieira.daniel.ExcelConsumer.integration;

import com.vieira.daniel.ExcelConsumer.service.excel.ExcelConsumerService;
import java.io.File;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
public class BasicIntegrationConfig {

    @Value("${app.input.folder}")
    private String inputDir;

    @Value("${app.output.folder}")
    private String outputDir;

    @Value("${app.file.pattern}")
    private String filePattern;

    @Autowired
    private ExcelConsumerService service;

    @Bean
    public MessageChannel fileChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource sourceReader = new FileReadingMessageSource();
        sourceReader.setDirectory(new File(inputDir));
        sourceReader.setFilter(new SimplePatternFileListFilter(filePattern));
        return sourceReader;
    }


    @Bean
    @ServiceActivator(inputChannel = "fileChannel")
    public MessageHandler fileWritingMessageHandler() {
        log.info("output directory: {}", outputDir);

        return new FileHandler(outputDir, service);
    }


}
