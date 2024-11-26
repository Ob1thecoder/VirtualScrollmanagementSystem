package com.VSS.controller;



import java.util.List;

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
    public ResponseEntity<Scroll> uploadScroll(
            @RequestParam("title") String title,
            @RequestParam("owner") String owner,
            @RequestParam("file") MultipartFile file) {
        try {
            byte[] fileData = file.getBytes();
            Scroll scroll = scrollService.uploadScroll(title, owner, fileData);

            scrollService.incrementUploadCount(scroll.getId());
            return new ResponseEntity<>(scroll, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<String> previewScroll(@PathVariable Long id) {
        Scroll scroll = scrollService.getScrollById(id);
        if (scroll == null) {
            return new ResponseEntity<>("Scroll not found", HttpStatus.NOT_FOUND);
        }

        String filePreview = new String(scroll.getFile());  
        return new ResponseEntity<>(filePreview, HttpStatus.OK);
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
        Scroll scroll = scrollService.downloadScroll(id);
        if (scroll == null) {
            return ResponseEntity.notFound().build();
        }

        // scrollService.incrementDownloadCount(id);

        

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + scroll.getTitle() + ".txt\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(scroll.getFile().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(scroll.getFile()));
    }


    
}
