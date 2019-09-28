package com.sample.spring.file.util;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileProcessor {
    private static final String HEADER_FILE_NAME = "file_name";
    private static final String MSG1 = "%s received. Content: %s";
    private static final String MSG2 = "deleted file %s after processing";

    public void process(Message<String> msg) {
        String fileName = (String) msg.getHeaders().get(HEADER_FILE_NAME);
        String content = msg.getPayload();
        System.out.println(String.format(MSG1, fileName, content));
        //Deletes file after processing
        File file = new File("./test-files/" + fileName);
        if (file.delete()) {
            System.out.println(String.format(MSG2, fileName));
        }

    }
}
