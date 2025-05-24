package com.shop.utils;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileUtilsTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private String testFilePath;
    private String testContent;
    
    @Before
    public void setUp() throws IOException {
        tempFolder.create();
        File testFile = tempFolder.newFile("test.txt");
        testFilePath = testFile.getAbsolutePath();
        testContent = "Test content\nSecond line";
    }
    
    @After
    public void tearDown() {
        tempFolder.delete();
    }
    
    @Test
    public void testWriteAndReadFile() throws IOException {
        FileUtils.writeToFile(testFilePath, testContent);
        String readContent = FileUtils.readFromFile(testFilePath);
        // Normalize line endings to account for different OS preferences
        String normalizedExpected = testContent.replace("\n", System.lineSeparator());
        // The FileUtils adds a trailing line separator, so we need to add it to our expected content
        assertEquals(normalizedExpected + System.lineSeparator(), readContent);
    }
    
    @Test
    public void testFileExists() throws IOException {
        assertTrue(FileUtils.fileExists(testFilePath));
        assertFalse(FileUtils.fileExists(testFilePath + ".nonexistent"));
    }
    
    @Test
    public void testCreateDirectory() throws IOException {
        String dirPath = tempFolder.getRoot().getAbsolutePath() + "/testDir";
        FileUtils.createDirectory(dirPath);
        File dir = new File(dirPath);
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
    }
    
    @Test
    public void testListFiles() throws IOException {
        // Create a directory with some files
        String dirPath = tempFolder.getRoot().getAbsolutePath() + "/listFilesTest";
        FileUtils.createDirectory(dirPath);
        
        // Create some files
        FileUtils.writeToFile(dirPath + "/file1.txt", "content1");
        FileUtils.writeToFile(dirPath + "/file2.txt", "content2");
        
        List<String> files = FileUtils.listFiles(dirPath);
        assertEquals(2, files.size());
        assertTrue(files.contains("file1.txt"));
        assertTrue(files.contains("file2.txt"));
    }
    
    @Test
    public void testDeleteFile() throws IOException {
        assertTrue(FileUtils.fileExists(testFilePath));
        FileUtils.deleteFile(testFilePath);
        assertFalse(FileUtils.fileExists(testFilePath));
    }
} 