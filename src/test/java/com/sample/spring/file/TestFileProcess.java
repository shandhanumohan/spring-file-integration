package com.sample.spring.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

@SpringBootTest
public class TestFileProcess {
    private final int fileCount = 5;
    private final Log logger = LogFactory.getLog(TestFileProcess.class);

    @Before
    public void createDirectory(){
        File directory = new File("./test-files");
        if (directory.exists()){
            directory.delete();
        }
        directory.mkdir();
    }

    @Test
    public void testCreateDummyFiles() throws Exception {
        for (int i = 0; i < fileCount; i++) {
            File file = new File("./test-files/file_" + i + ".txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write("hello " + i);
            out.close();
        }
        logger.info("Created dummy files");

    }
}
