package com.VSS.controller;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.VSS.model.Scroll;
import com.VSS.service.ScrollService;

public class ScrollControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ScrollService scrollService;

    @InjectMocks
    private ScrollController scrollController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(scrollController).build();
    }

    // list all scrolls
    @Test
    public void testListOrSearchScrolls() throws Exception {
        // Mock scroll data
        Scroll scroll1 = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-22");
        Scroll scroll2 = new Scroll(2L, "Harry Xu", "Test".getBytes(), "user2", "2024-10-22");

        // Mock the behavior of scrollService.searchScrolls()
        when(scrollService.searchScrolls(null, null, null, null)).thenReturn(Arrays.asList(scroll1, scroll2));

        // Perform the GET request
        mockMvc.perform(get("/api/admin/scrolls/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Harry Xu"))
                .andExpect(jsonPath("$[0].owner").value("user1"))
                .andExpect(jsonPath("$[0].uploadedAt").value("2024-10-22"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Harry Xu"))
                .andExpect(jsonPath("$[1].owner").value("user2"))
                .andExpect(jsonPath("$[1].uploadedAt").value("2024-10-22"));

        verify(scrollService, times(1)).searchScrolls(null, null, null, null);
    }

    // list scrolls by owner
    @Test
    public void testListScrollsByUser() throws Exception {
        // Mock scroll data
        Scroll scroll1 = new Scroll(1L, "Harry Xu", "Test".getBytes(), "Xu", "2024-10-22");

        // Mock the behavior of scrollService.searchScrolls()
        when(scrollService.getScrollsByOwner("Xu")).thenReturn(Arrays.asList(scroll1));

        // Perform the GET request
        mockMvc.perform(get("/api/admin/scrolls/list/Xu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Harry Xu"))
                .andExpect(jsonPath("$[0].owner").value("Xu"))
                .andExpect(jsonPath("$[0].uploadedAt").value("2024-10-22"));

        verify(scrollService, times(1)).getScrollsByOwner("Xu");
    }

    // list null scroll by owner
    @Test
    public void testNullListScrollsByUser() throws Exception {
        // Mock the behavior of scrollService.searchScrolls()
        when(scrollService.getScrollsByOwner("Xu")).thenReturn(Arrays.asList());

        // Perform the GET request
        mockMvc.perform(get("/api/admin/scrolls/list/Xu"))
                .andExpect(status().isNoContent());

        verify(scrollService, times(1)).getScrollsByOwner("Xu");
    }

    // search scroll
    @Test
    public void testSearchScrolls() throws Exception {
        // Mock scroll data
        Scroll scroll1 = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-22");

        // Mock the behavior of scrollService.searchScrolls()
        when(scrollService.searchScrolls("user1", 1L, "Harry Xu", "2024-10-22")).thenReturn(Arrays.asList(scroll1));

        // Perform the GET request with query parameters
        mockMvc.perform(get("/api/admin/scrolls/search")
                .param("uploaderId", "user1")
                .param("scrollId", "1")
                .param("title", "Harry Xu")
                .param("uploadDate", "2024-10-22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Harry Xu"))
                .andExpect(jsonPath("$[0].owner").value("user1"))
                .andExpect(jsonPath("$[0].uploadedAt").value("2024-10-22"));

        verify(scrollService, times(1)).searchScrolls("user1", 1L, "Harry Xu", "2024-10-22");
    }

    // upload scroll
    @Test
    public void testUploadScroll() throws Exception {
        // Mock scroll data
        Scroll scroll = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-22");

        // Mock the behavior of scrollService.uploadScroll()
        when(scrollService.uploadScroll("Harry Xu", "user1", "Test".getBytes())).thenReturn(scroll);

        // Perform the POST request using multipart
        mockMvc.perform(multipart("/api/admin/scrolls/upload")
                .file(new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test".getBytes()))
                .param("title", "Harry Xu")
                .param("owner", "user1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Harry Xu"))
                .andExpect(jsonPath("$.owner").value("user1"))
                .andExpect(jsonPath("$.uploadedAt").value("2024-10-22"));

        verify(scrollService, times(1)).uploadScroll("Harry Xu", "user1", "Test".getBytes());
    }

    // upload scroll with exception
   
    
    // preview null scroll
    @Test
    public void testPreviewNullScroll() throws Exception {
        // Mock the behavior of scrollService.getScrollById()
        when(scrollService.getScrollById(1L)).thenReturn(null);

        // Perform the GET request
        mockMvc.perform(get("/api/admin/scrolls/preview/1"))
                .andExpect(status().isNotFound());

        verify(scrollService, times(1)).getScrollById(1L);
    }

    // delete scroll by ID
    @Test
    public void testDeleteScroll() throws Exception {
        Scroll scroll = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-22");

        when(scrollService.getScrollById(1L)).thenReturn(scroll);

        doNothing().when(scrollService).deleteScroll(1L);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/admin/scrolls/delete/1"))
                .andExpect(status().isNoContent());

        verify(scrollService, times(1)).deleteScroll(1L);
    }

    // delete null scroll by ID
    @Test
    public void testDeleteNullScroll() throws Exception {
        when(scrollService.getScrollById(1L)).thenReturn(null);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/admin/scrolls/delete/1"))
                .andExpect(status().isNotFound());

        verify(scrollService, times(0)).deleteScroll(1L);
    }

    // edit scroll by ID
    @Test
    public void testEditScroll() throws Exception {
        Scroll scroll = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-22");

        when(scrollService.getScrollById(1L)).thenReturn(scroll);

        doNothing().when(scrollService).updateScrollContent(1L, "New content");

        // Perform the POST request
        mockMvc.perform(post("/api/admin/scrolls/edit/1")
                .param("file_content", "New content"))
                .andExpect(status().isOk())
                .andExpect(content().string("Scroll updated successfully"));

        verify(scrollService, times(1)).updateScrollContent(1L, "New content");
    }

    // edit null scroll by ID
    @Test
    public void testEditNullScroll() throws Exception {
        when(scrollService.getScrollById(1L)).thenReturn(null);

        // Perform the POST request
        mockMvc.perform(post("/api/admin/scrolls/edit/1")
                .param("file_content", "New content"))
                .andExpect(status().isNotFound());

        verify(scrollService, times(0)).updateScrollContent(1L, "New content");
    }

    // download scroll by ID
    // @Test
    // public void testDownloadScroll() throws Exception {
    //     Scroll scroll = new Scroll(1L, "Harry Xu", "Test".getBytes(), "user1", "2024-10-22");

    //     when(scrollService.downloadScroll(1L)).thenReturn(scroll);

    //     // Perform the GET request
    //     mockMvc.perform(get("/api/admin/scrolls/download/1"))
    //             .andExpect(status().isOk())
    //             .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Harry Xu.txt\""))
    //             .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
    //             .andExpect(content().bytes(scroll.getFile())); // Validate the response body

    //     verify(scrollService, times(1)).downloadScroll(1L);
    // }

    // download null scroll by ID
    // @Test
    // public void testDownloadNullScroll() throws Exception {
    //     when(scrollService.downloadScroll(1L)).thenReturn(null);

    //     // Perform the GET request
    //     mockMvc.perform(get("/api/admin/scrolls/download/1"))
    //             .andExpect(status().isNotFound());

    //     verify(scrollService, times(1)).downloadScroll(1L);
    // }

}