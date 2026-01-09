package com.VSS.controller;



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

    @GetMapping("/list")
    public ResponseEntity<List<Scroll>> listOrSearchScrolls(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String uploadedAt) {

        List<Scroll> scrolls = scrollService.searchScrolls(owner, id, title, uploadedAt);
        return new ResponseEntity<>(scrolls, HttpStatus.OK);
    }
    @GetMapping("/popular")
    public ResponseEntity<List<Scroll>> getPopularScrolls() {
        List<Scroll> popularScrolls = scrollService.getPopularScroll();
        return new ResponseEntity<>(popularScrolls, HttpStatus.OK);
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
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                return new ResponseEntity<>("Invalid filename. File must have an extension.",
                        HttpStatus.BAD_REQUEST);
            }
            
            String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            if (!(fileType.equals("txt") || fileType.equals("pdf") || fileType.equals("jpg") || fileType.equals("png") || fileType.equals("jpeg"))) {
                return new ResponseEntity<>("Unsupported file type. Allowed types: .txt, .pdf, .jpg, .jpeg, .png.",
                        HttpStatus.BAD_REQUEST);
            }
            
            // Validate file size (10MB limit)
            long maxFileSize = 10 * 1024 * 1024; // 10MB
            if (file.getSize() > maxFileSize) {
                return new ResponseEntity<>("File size exceeds maximum limit of 10MB.",
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

    // GET endpoint for previewing a scroll file 
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
        try {
            Scroll scroll = scrollService.getScrollById(id); 
            if (scroll == null) {
                return new ResponseEntity<>("Scroll not found", HttpStatus.NOT_FOUND);
            }

            scrollService.updateScrollContent(id, fileContent);
            return new ResponseEntity<>("Scroll updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating scroll: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        String fileName = scroll.getTitle() + "." +scroll.getfileType();  
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
    
        // Return the file as a downloadable resource
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(scroll.getFile().length)  
                .contentType(MediaType.APPLICATION_OCTET_STREAM)  
                .body(new ByteArrayResource(scroll.getFile()));
    }

    @PostMapping("/like")
    public ResponseEntity<String> likeScroll(
            @RequestParam("userId") Long userId,
            @RequestParam("scrollId") Long scrollId) {
        try {
            scrollService.addFavourite(userId, scrollId);
            return new ResponseEntity<>("Scroll liked successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error liking scroll: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/likes")
    public ResponseEntity<List<Scroll>> getLikesByUserId(@RequestParam("userId") Long userId) {
        try {
            List<Scroll> likedScrolls = scrollService.getLikesByUserId(userId);
            return new ResponseEntity<>(likedScrolls, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/unlike")
    public ResponseEntity<String> unlikeScroll(
            @RequestParam("userId") Long userId,
            @RequestParam("scrollId") Long scrollId) {
        try {
            scrollService.removeFavourite(userId, scrollId);
            return new ResponseEntity<>("Scroll unliked successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error unliking scroll: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    
}
