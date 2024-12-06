package com.VSS.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.VSS.model.Scroll;
import com.VSS.util.ScrollRespository;


@Service
public class ScrollService {

    private final ScrollRespository scrollRepository;

    public ScrollService(ScrollRespository scrollRepository) {
        this.scrollRepository = scrollRepository;
    }

    public Scroll uploadScroll(String title, String owner, byte[] fileData, String fileType) {
        Scroll scroll = new Scroll(null, title, fileData, owner, null, fileType);
        System.out.println("Title: " + title);
        System.out.println("Owner: " + owner);
        System.out.println("File Type: " + fileType);

        scrollRepository.saveScroll(scroll);
        scrollRepository.incrementUploadCount(scroll.getId());
        return scroll;
    }
    public void updateScroll(Scroll updatedScroll) {
        scrollRepository.updateScroll(updatedScroll);
    }

    public Scroll downloadScroll(Long id) {
        Scroll scroll = scrollRepository.findScrollById(id);

        if (scroll != null) {
            scrollRepository.incrementDownloadCount(id);  
        }

        return scroll;
    }

    // List all scrolls
    public List<Scroll> getAllScrolls() {
        return scrollRepository.findAll();
    }

    

    // // Upload a scroll
    // public void uploadScroll(String title, MultipartFile file, String owner) throws IOException {
    //     scrollRepository.saveScroll(title, file, owner);
    // }
    // Add a new scroll
    public int addScroll(Scroll scroll) {
        return scrollRepository.saveScroll(scroll);
    }
    // Delete scroll by ID
    public void deleteScroll(Long id) {
        scrollRepository.deleteScrollById(id);
    }
    // Update Scroll
    public void updateScrollContent(Long id, String newContent) {
        Scroll scroll = scrollRepository.findScrollById(id);
        if (scroll != null) {
            scroll.setFile(newContent.getBytes());  
            scrollRepository.updateScroll(scroll);  
        }
    }
    
    public void incrementUploadCount(Long id) {
        scrollRepository.incrementUploadCount(id);  
    }

    // Increment download count for a scroll
    public void incrementDownloadCount(Long id) {
        scrollRepository.incrementDownloadCount(id);  
    }
    
    

    public List<Scroll> getScrollsByOwner(String owner) {
        return scrollRepository.findScrollsByOwner(owner);
    }

    // Get scroll by ID
    public Scroll getScrollById(Long id) {
        return scrollRepository.findScrollById(id);
    }

    public List<Scroll> searchScrolls(String uploaderId, Long scrollId, String title, String uploadDate) {
        return scrollRepository.searchScrolls(uploaderId, scrollId, title, uploadDate);
    }
}


