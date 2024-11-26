package com.VSS.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Scroll {
    private Long id;
    private String title;
    private byte[] file;  
    private String owner;
    private String uploadedAt;
    private int uploadCount;  
    private int downloadCount;  
    
    public Scroll() {}

    public Scroll(Long id, String title, byte[] file, String owner, String uploadedAt) {
        this.id = id;
        this.title = title;
        this.file = file;
        this.owner = owner;
        this.uploadedAt = uploadedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public byte[] getFile() { return file; }
    public void setFile(byte[] file) { this.file = file; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(String uploadedAt) { this.uploadedAt = uploadedAt; }
    public static byte[] fileToBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }
    
    public static File bytesToFile(byte[] bytes, String outputFilePath) throws IOException {
        File file = new File(outputFilePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
     
        }
        return file;
    }
}
