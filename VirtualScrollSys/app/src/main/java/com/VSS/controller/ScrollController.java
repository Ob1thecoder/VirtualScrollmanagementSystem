package com.VSS.controller;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.VSS.model.Scroll;
import com.VSS.service.ScrollService;

@RestController
@RequestMapping("/api/admin/scrolls")
public class ScrollController {

    private final ScrollService scrollService;

    public ScrollController(ScrollService scrollService) {
        this.scrollService = scrollService;
    }

    // GET endpoint to list all scrolls
    // @GetMapping("/list")
    // public ResponseEntity<List<Scroll>> listAllScrolls() {
    //     List<Scroll> scrolls = scrollService.getAllScrolls();
    //     return new ResponseEntity<>(scrolls, HttpStatus.OK);
    // }

    @GetMapping("/list")
    public ResponseEntity<List<Scroll>> listOrSearchScrolls(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String uploadedAt) {

        List<Scroll> scrolls = scrollService.searchScrolls(owner, id, title, uploadedAt);
        return new ResponseEntity<>(scrolls, HttpStatus.OK);
    }

    // POST endpoint to upload a new scroll
    @PostMapping("/upload")
    public ResponseEntity<?> uploadScroll(
            @RequestParam("title") String title,
            @RequestParam("owner") String owner,
            @RequestParam("file") MultipartFile file) {
        try {
            // Check for null or empty values
            if (title == null || title.isEmpty() || owner == null || owner.isEmpty()) {
                return new ResponseEntity<>("Title and owner are required.", HttpStatus.BAD_REQUEST);
            }

            // Get file content and validate
            byte[] fileData = file.getBytes();
            if (fileData.length == 0) {
                return new ResponseEntity<>("File is empty.", HttpStatus.BAD_REQUEST);
            }

            // Extract file type and validate
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            if (!(fileType.equals("txt") || fileType.equals("pdf") || fileType.equals("jpg") || fileType.equals("png"))) {
                return new ResponseEntity<>("Unsupported file type. Allowed types: .txt, .pdf, .jpg, .png.",
                        HttpStatus.BAD_REQUEST);
            }

            // Upload logic
            Scroll scroll = scrollService.uploadScroll(title, owner, fileData, fileType);
            return new ResponseEntity<>(scroll, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error uploading file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/search")
    public ResponseEntity<List<Scroll>> searchScrolls(
            @RequestParam(required = false) String uploaderId,
            @RequestParam(required = false) Long scrollId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String uploadDate) {

        List<Scroll> scrolls = scrollService.searchScrolls(uploaderId, scrollId, title, uploadDate);
        return ResponseEntity.ok(scrolls);
    }

    // GET endpoint for previewing a scroll file (text or binary preview)
    @GetMapping("/preview/{id}")
public ResponseEntity<Map<String, Object>> previewScroll(@PathVariable Long id) {
    Scroll scroll = scrollService.getScrollById(id);
    if (scroll == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Map<String, Object> response = new HashMap<>();
    response.put("fileType", scroll.getfileType());
    System.out.println(scroll.getfileType());
    response.put("file", Base64.getEncoder().encodeToString(scroll.getFile()));

    return new ResponseEntity<>(response, HttpStatus.OK);
}

    //Check file types
    private String detectMimeType(String fileName, byte[] fileData) {
        try {
            return Files.probeContentType(Path.of(fileName));
        } catch (IOException e) {
            try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
                return URLConnection.guessContentTypeFromStream(inputStream);
            } catch (IOException ex) {
                return "application/octet-stream";
            }
    }
}

    @GetMapping("/list/{username}")
    public ResponseEntity<List<Scroll>> listScrollsByUser(@PathVariable String username) {
        List<Scroll> scrolls = scrollService.getScrollsByOwner(username);
        if (scrolls.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  
        }
        return new ResponseEntity<>(scrolls, HttpStatus.OK);  
    }

    // DELETE endpoint to delete a scroll by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteScroll(@PathVariable Long id) {
        Scroll scroll = scrollService.getScrollById(id);
        if (scroll == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
        }

        // Delete the scroll without any ownership check
        scrollService.deleteScroll(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editScroll(
            @PathVariable Long id,  
            @RequestParam("file_content") String fileContent) {

        Scroll scroll = scrollService.getScrollById(id); 
        if (scroll == null) {
            return new ResponseEntity<>("Scroll not found", HttpStatus.NOT_FOUND);
        }

        scrollService.updateScrollContent(id, fileContent);
        return new ResponseEntity<>("Scroll updated successfully", HttpStatus.OK);

        
    }

    

    


    // GET endpoint for downloading a scroll by its ID
    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadScroll(@PathVariable Long id) {
        // Retrieve the scroll by ID
        Scroll scroll = scrollService.getScrollById(id);
        if (scroll == null) {
            // If no scroll is found, return a 404 response
            return ResponseEntity.notFound().build();
        }
    
        // Increment download count for tracking purposes
        scrollService.incrementDownloadCount(id);
    
        // Set appropriate headers for file download
        HttpHeaders headers = new HttpHeaders();
        String fileName = scroll.getTitle() + ".txt";  
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
    
        // Return the file as a downloadable resource
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(scroll.getFile().length)  
                .contentType(MediaType.APPLICATION_OCTET_STREAM)  
                .body(new ByteArrayResource(scroll.getFile()));
    }
    

    
}
