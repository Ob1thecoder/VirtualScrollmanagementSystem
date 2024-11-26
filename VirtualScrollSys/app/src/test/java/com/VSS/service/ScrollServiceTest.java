package com.VSS.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.VSS.model.Scroll;
import com.VSS.util.ScrollRespository;

public class ScrollServiceTest {
    @Mock
    private ScrollRespository scrollRepository;

    @InjectMocks
    private ScrollService scrollService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllScrolls() {
        // Mock scroll data
        List<Scroll> mockScrolls = Arrays.asList(
                new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-12"),
                new Scroll(2L, "Harry Potter", "Test2".getBytes(), "user2", "2024-10-12"));

        when(scrollRepository.findAll()).thenReturn(mockScrolls);

        // Call the service method
        List<Scroll> scrolls = scrollService.getAllScrolls();

        // Assertions
        assertEquals(2, scrolls.size(), "Expected two scrolls");
        assertEquals("Harry Xu", scrolls.get(0).getTitle(), "First scroll's title should be 'Harry Xu'");
        verify(scrollRepository, times(1)).findAll();
    }

    @Test
    public void testAddScroll() {
        Scroll scroll = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-12");

        scrollService.addScroll(scroll);

        verify(scrollRepository, times(1)).saveScroll(scroll);
    }

    @Test
    public void testDeleteScroll() {
        // Mock a scroll ID
        Long scrollId = 1L;

        scrollService.deleteScroll(scrollId);

        verify(scrollRepository, times(1)).deleteScrollById(scrollId);
    }

    @Test
    public void testGetScrollById() {
        // Mock a scroll
        Scroll mockScroll = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-21");

        when(scrollRepository.findScrollById(1L)).thenReturn(mockScroll);

        // Call the service method
        Scroll scroll = scrollService.getScrollById(1L);

        // Assertions
        assertEquals("Harry Xu", scroll.getTitle(), "Scroll's title should be 'Harry Xu'");
        verify(scrollRepository, times(1)).findScrollById(1L);
    }

    @Test
    public void testGetScrollsByOwner() {
        // Mock scroll data
        List<Scroll> mockScrolls = Arrays.asList(
                new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-12"),
                new Scroll(2L, "Harry Potter", "Test2".getBytes(), "user1", "2024-10-12"));

        when(scrollRepository.findScrollsByOwner("user1")).thenReturn(mockScrolls);

        // Call the service method
        List<Scroll> scrolls = scrollService.getScrollsByOwner("user1");

        // Assertions
        assertEquals(2, scrolls.size(), "Expected two scrolls");
        assertEquals("Harry Xu", scrolls.get(0).getTitle(), "First scroll's title should be 'Harry Xu'");
        assertEquals("Harry Potter", scrolls.get(1).getTitle(), "First scroll's title should be 'Harry Potter'");
        verify(scrollRepository, times(1)).findScrollsByOwner("user1");
    }

    @Test
    public void testSearchScrolls() {
        // Mock scroll data
        List<Scroll> mockScrolls = Arrays.asList(
                new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-12"),
                new Scroll(2L, "Harry Potter", "Test2".getBytes(), "user2", "2024-10-12"));

        when(scrollRepository.searchScrolls("user1", 1L, "Harry Xu", "2024-10-12")).thenReturn(mockScrolls);

        // Call the service method
        List<Scroll> scrolls = scrollService.searchScrolls("user1", 1L, "Harry Xu", "2024-10-12");

        // Assertions
        assertEquals(2, scrolls.size(), "Expected two scrolls");
        assertEquals("Harry Xu", scrolls.get(0).getTitle(), "First scroll's title should be 'Harry Xu'");
        assertEquals("Harry Potter", scrolls.get(1).getTitle(), "First scroll's title should be 'Harry Potter'");
        verify(scrollRepository, times(1)).searchScrolls("user1", 1L, "Harry Xu", "2024-10-12");
    }

    /*
     * Once there does not assign a scroll use findScrollById function,
     * then use the argument captor to capture the scroll object
     */
    @Test
    public void testUpdateScroll() {
        // Create an ArgumentCaptor to capture the Scroll object passed to updateScroll
        ArgumentCaptor<Scroll> scrollCaptor = ArgumentCaptor.forClass(Scroll.class);

        // Call the service method with the updated scroll
        Scroll newScroll = new Scroll(1L, "New title", "Test".getBytes(), "user1", "2024-10-22");
        scrollService.updateScroll(newScroll);

        // Verify that updateScroll was called with the correct Scroll object
        verify(scrollRepository, times(1)).updateScroll(scrollCaptor.capture());

        // Get the captured Scroll object
        Scroll capturedScroll = scrollCaptor.getValue();

        // Assertions
        assertEquals("New title", capturedScroll.getTitle(), "Scroll's title should be 'New title'");
        assertEquals("2024-10-22", capturedScroll.getUploadedAt(), "Scroll's uploaded date should be '2024-10-22'");
    }

    @Test
    public void testUpdateScrollContent() {
        // Mock a scroll
        Scroll mockScroll = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-21");

        when(scrollRepository.findScrollById(1L)).thenReturn(mockScroll);

        // Call the service method
        scrollService.updateScrollContent(1L, "New content");

        // Assertions
        assertEquals("New content", new String(mockScroll.getFile()), "Scroll's content should be 'New content'");
        verify(scrollRepository, times(1)).updateScroll(mockScroll);
    }

    @Test
    public void testUploadScroll() {
        // Call the service method
        Scroll file = scrollService.uploadScroll("Harry Xu", "user1", "Test".getBytes());

        when(scrollRepository.saveScroll(any(Scroll.class))).thenReturn(1);

        // Assertions
        assertEquals(null, file.getId(), "Scroll's ID should be null");
        assertEquals("Harry Xu", file.getTitle(), "Scroll's title should be 'Harry Xu'");
        assertEquals("user1", file.getOwner(), "Scroll's owner should be 'user1'");
        assertEquals("Test", new String(file.getFile()), "Scroll's content should be 'Test'");
        assertEquals(null, file.getUploadedAt(), "Scroll's uploaded date should be null");
        verify(scrollRepository, times(1)).saveScroll(file);
    }

    @Test
    public void testUploadNullScrollContent() {
        // Mock a scroll
        Scroll mockScroll = null;

        when(scrollRepository.findScrollById(1L)).thenReturn(mockScroll);

        // Call the service method
        scrollService.updateScrollContent(1L, "New content");

        // Assertions
        verify(scrollRepository, times(0)).updateScroll(mockScroll);
    }

    @Test
    public void testdownloadScroll() {
        // Mock a scroll
        Scroll mockScroll = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-21");

        when(scrollRepository.findScrollById(1L)).thenReturn(mockScroll);

        // Call the service method
        Scroll file = scrollService.downloadScroll(1L);

        // Assertions
        assertEquals(1L, file.getId(), "Scroll's ID should be 1");
        assertEquals("Harry Xu", file.getTitle(), "Scroll's title should be 'Harry Xu'");
        assertEquals("user1", file.getOwner(), "Scroll's owner should be 'user1'");
        assertEquals("Test", new String(file.getFile()), "Scroll's content should be 'Test'");
        assertEquals("2024-10-21", file.getUploadedAt(), "Scroll's uploaded date should be '2024-10-21'");
        verify(scrollRepository, times(1)).findScrollById(1L);
    }

    @Test
    public void testdownloadNullScroll() {
        // Mock a scroll
        Scroll mockScroll = null;

        when(scrollRepository.findScrollById(1L)).thenReturn(mockScroll);

        // Call the service method
        Scroll file = scrollService.downloadScroll(1L);

        // Assertions
        assertEquals(null, file, "Scroll should be null");
        verify(scrollRepository, times(1)).findScrollById(1L);
    }

    @Test
    public void testincrementUploadCount() {
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

        // Call the service method
        scrollService.incrementUploadCount(1L);

        // Verify that incrementUploadCount was called with the correct ID
        verify(scrollRepository, times(1)).incrementUploadCount(idCaptor.capture());
    }

    @Test
    public void testincrementDownloadCount() {
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);

        // Call the service method
        scrollService.incrementDownloadCount(1L);

        // Verify that incrementDownloadCount was called with the correct ID
        verify(scrollRepository, times(1)).incrementDownloadCount(idCaptor.capture());

    }
}