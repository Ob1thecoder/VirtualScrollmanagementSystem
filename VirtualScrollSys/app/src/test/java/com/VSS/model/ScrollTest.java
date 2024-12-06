package com.VSS.model;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScrollTest {

    private Long id;
    private String title;
    private byte[] fileContent;
    private String owner;
    private String uploadedAt;
    private Scroll scroll;
    private String fileType;

    @BeforeEach
    public void setUp() {
        this.id = 2L;
        this.title = "Constructor Scroll";
        this.fileContent = "Another content".getBytes();
        this.owner = "user2";
        this.uploadedAt = "2024-10-17";
        this.fileType = ".txt";

        scroll = new Scroll(id, title, fileContent, owner, uploadedAt, fileType);
    }

    @Test
    public void testDefaultConstructor() {
        // Create a new Scroll object using the default constructor
        Scroll defaultScroll = new Scroll();

        // Verify that all fields are initialized to their default values
        assertNull(defaultScroll.getId(), "Expected 'id' to be null");
        assertNull(defaultScroll.getTitle(), "Expected 'title' to be null");
        assertNull(defaultScroll.getFile(), "Expected 'file' to be null");
        assertNull(defaultScroll.getOwner(), "Expected 'owner' to be null");
        assertNull(defaultScroll.getUploadedAt(), "Expected 'uploadedAt' to be null");
    }

    @Test
    public void testConstructor() {
        // Verify that the constructor sets fields correctly
        assertEquals(id, scroll.getId());
        assertEquals(title, scroll.getTitle());
        assertArrayEquals(fileContent, scroll.getFile());
        assertEquals(owner, scroll.getOwner());
        assertEquals(uploadedAt, scroll.getUploadedAt());
    }

    @Test
    public void testGettersAndSetters() {
        // Verify that the getters and setters work correctly
        Long newId = 3L;
        String newTitle = "New Title";
        byte[] newFileContent = "New content".getBytes();
        String newOwner = "user3";
        String newUploadedAt = "2024-10-18";

        scroll.setId(newId);
        scroll.setTitle(newTitle);
        scroll.setFile(newFileContent);
        scroll.setOwner(newOwner);
        scroll.setUploadedAt(newUploadedAt);
        scroll.setUploadCount(1);
        scroll.setDownloadCount(2);

        assertEquals(newId, scroll.getId());
        assertEquals(newTitle, scroll.getTitle());
        assertArrayEquals(newFileContent, scroll.getFile());
        assertEquals(newOwner, scroll.getOwner());
        assertEquals(newUploadedAt, scroll.getUploadedAt());
        assertEquals(1, scroll.getUploadCount());
        assertEquals(2, scroll.getDownloadCount());
    }

    @Test
    public void testFileToBytes() throws Exception {
        // Verify that the fileToBytes method works correctly
        File file = new File("test.txt");
        byte[] fileContent = "Test content".getBytes();
        Files.write(file.toPath(), fileContent);
        byte[] bytes = Scroll.fileToBytes(file);
        assertArrayEquals(fileContent, bytes);

        // Clean up
        file.delete();
    }

    @Test
    public void testBytesToFile() throws Exception {
        // Verify that the bytesToFile method works correctly
        byte[] bytes = "Test content".getBytes();
        String outputFilePath = "output.txt";
        File file = Scroll.bytesToFile(bytes, outputFilePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        assertArrayEquals(bytes, fileContent);

        // Clean up
        file.delete();
    }
}