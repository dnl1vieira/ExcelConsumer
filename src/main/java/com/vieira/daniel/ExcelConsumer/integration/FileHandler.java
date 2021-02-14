package com.vieira.daniel.ExcelConsumer.integration;

import static java.lang.String.format;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.move;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.apache.commons.io.FileUtils.copyFile;

import java.io.File;

import com.vieira.daniel.ExcelConsumer.service.excel.ExcelConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;

@Slf4j
public class FileHandler implements MessageHandler {

    private final String outputDir;
    private ExcelConsumerService service;

    public FileHandler(String outputDir, ExcelConsumerService service) {
        this.outputDir = outputDir;
        this.service = service;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        File file = extractFile(message);
        processFile(file);
    }

    private File extractFile(Message<?> message) {
        Assert.notNull(message, "Message cannot be null");

        Object payload = message.getPayload();

        log.info("Message recived file {}", payload);

        if (!(payload instanceof File))
            throw new MessagingException("Invalid Payload");

        return (File) payload;
    }

    private void processFile(File file) {
        try {
            log.debug("Converting to file");
            service.process(file.toPath());
            moveFile(file, file.getName());
        } catch (Exception e) {
            moveFile(file, file.getName());
            log.error("Unnable to read file", e);
        }
    }

    private void moveFile(File file, String filename) {
        try {
            String fileDestinationName = format("%s%s_%s", outputDir, file.getName(),
                    now().format(ofPattern("yyyyMMddHHmmssS")));

            File destination = new File(fileDestinationName);

            copyFile(file, destination);

            delete(file.toPath());
        } catch (Exception e) {
            log.error("Unnable to move file", e);
        }
    }

}
