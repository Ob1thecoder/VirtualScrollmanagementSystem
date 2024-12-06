package com.VSS.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.VSS.model.Scroll;

public class ScrollRespositoryTest {

    private JdbcTemplate jdbcTemplate;
    private ScrollRespository scrollRepository;

    @BeforeEach
    public void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        scrollRepository = new ScrollRespository(jdbcTemplate);
    }

    // @Test
    // public void testSaveScroll() {
    //     Scroll scroll = new Scroll();
    //     scroll.setTitle("Test Title");
    //     scroll.setOwner("Test Owner");
    //     scroll.setFile(new byte[0]); // Simulate an empty file
    
    //     // Mock the behavior of jdbcTemplate's update method to return 1 (indicating success)
    //     when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), any())).thenReturn(1);
    
    //     // Call the saveScroll method
    //     int result = scrollRepository.saveScroll(scroll);
    
    //     // Assert that the result is 1 (indicating that 1 row was affected)
    //     assertEquals(1, result);
    
    //     // Verify that jdbcTemplate's update method was called exactly once, with the expected arguments
    //     verify(jdbcTemplate, times(1)).update(anyString(), any(), any(), any(), any(), any());
    // }
    

    @Test
    public void testFindAll() {
        // Create a mock Scroll object
        Scroll mockScroll = new Scroll();
        mockScroll.setId(1L);
        mockScroll.setTitle("Test Title");
        mockScroll.setFile("This is the file content".getBytes());
        mockScroll.setOwner("Test Owner");
        mockScroll.setUploadedAt("2024-10-22");
        mockScroll.setUploadCount(5);
        mockScroll.setDownloadCount(10);

        // Mock the behavior of jdbcTemplate.query()
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(mockScroll));

        // Call the findAll method
        List<Scroll> result = scrollRepository.findAll();

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        Scroll returnedScroll = result.get(0);
        assertEquals(mockScroll.getId(), returnedScroll.getId());
        assertEquals(mockScroll.getTitle(), returnedScroll.getTitle());
        assertArrayEquals(mockScroll.getFile(), returnedScroll.getFile());
        assertEquals(mockScroll.getOwner(), returnedScroll.getOwner());
        assertEquals(mockScroll.getUploadedAt(), returnedScroll.getUploadedAt());
        assertEquals(mockScroll.getUploadCount(), returnedScroll.getUploadCount());
        assertEquals(mockScroll.getDownloadCount(), returnedScroll.getDownloadCount());
    }

    @Test
    public void testUpdateScroll() {
        Scroll scroll = new Scroll();
        scroll.setId(1L);
        scroll.setTitle("Test Title");
        scroll.setOwner("Test Owner");
        scroll.setFile("This is the file content".getBytes()); // Assuming it's a byte array
        scroll.setUploadedAt("2024-10-22");

        when(jdbcTemplate.update(anyString(), any(), anyLong())).thenReturn(1);

        int result = scrollRepository.updateScroll(scroll);

        assertEquals(1, result);

        verify(jdbcTemplate, times(1)).update(eq("UPDATE scrolls SET file = ? WHERE id = ?"),
                eq(scroll.getFile()), eq(scroll.getId()));
    }

    @Test
    public void testFindScrollById() {
        Scroll mockScroll = new Scroll();
        mockScroll.setId(1L);
        mockScroll.setTitle("Test Title");
        mockScroll.setOwner("Test Owner");
        mockScroll.setFile("This is the file content".getBytes()); // Assuming it's a byte array
        mockScroll.setUploadedAt("2024-10-22");

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of(mockScroll));

        Scroll result = scrollRepository.findScrollById(1L);

        assertNotNull(result);

        assertEquals(mockScroll.getId(), result.getId(), "ID should match");
        assertEquals(mockScroll.getTitle(), result.getTitle(), "Title should match");
        assertEquals(mockScroll.getOwner(), result.getOwner(), "Owner should match");
        assertArrayEquals(mockScroll.getFile(), result.getFile(), "File content should match");
        assertEquals(mockScroll.getUploadedAt(), result.getUploadedAt(), "Uploaded At should match");

        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    public void testFindScrollByIdNotFound() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of());

        Scroll result = scrollRepository.findScrollById(1L);

        assertNull(result);

        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    public void testFindScrollsByOwner() {
        Scroll mockScroll = new Scroll();
        mockScroll.setId(1L);
        mockScroll.setTitle("Test Title");
        mockScroll.setOwner("Test Owner");
        mockScroll.setFile("This is the file content".getBytes()); // Assuming it's a byte array
        mockScroll.setUploadedAt("2024-10-22");

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of(mockScroll));

        List<Scroll> result = scrollRepository.findScrollsByOwner("Test Owner");

        assertEquals(1, result.size());

        Scroll returnedScroll = result.get(0);
        assertEquals(mockScroll.getId(), returnedScroll.getId(), "ID should match");
        assertEquals(mockScroll.getTitle(), returnedScroll.getTitle(), "Title should match");
        assertEquals(mockScroll.getOwner(), returnedScroll.getOwner(), "Owner should match");
        assertArrayEquals(mockScroll.getFile(), returnedScroll.getFile(), "File content should match");
        assertEquals(mockScroll.getUploadedAt(), returnedScroll.getUploadedAt(), "Uploaded At should match");

        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    @Test
    public void testIncrementUploadCount() {
        Long scrollId = 1L;

        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1); // Simulating that 1 row was updated

        int result = scrollRepository.incrementUploadCount(scrollId);

        assertEquals(1, result);

        // Verify that jdbcTemplate.update() was called with the correct SQL and
        // parameters
        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE scrolls SET upload_count = upload_count + 1 WHERE id = ?"),
                eq(scrollId));
    }

    @Test
    public void testIncrementDownloadCount() {
        Long scrollId = 1L;

        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1); // Simulating that 1 row was updated

        int result = scrollRepository.incrementDownloadCount(scrollId);

        assertEquals(1, result);

        // Verify that jdbcTemplate.update() was called with the correct SQL and
        // parameters
        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE scrolls SET download_count = download_count + 1 WHERE id = ?"),
                eq(scrollId));
    }

    @Test
    public void testDeleteScrollById() {
        Long scrollId = 1L;

        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1); // Simulating that 1 row was updated

        int result = scrollRepository.deleteScrollById(scrollId);

        assertEquals(1, result);

        // Verify that jdbcTemplate.update() was called with the correct SQL and
        // parameters
        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM scrolls WHERE id = ?"),
                eq(scrollId));
    }

    @Test
    public void testSearchScrolls() {
        // Create mock Scroll data
        Scroll mockScroll = new Scroll();
        mockScroll.setId(1L);
        mockScroll.setTitle("Test Title");
        mockScroll.setOwner("Test Owner");
        mockScroll.setFile("This is the file content".getBytes()); // Assuming it's a byte array
        mockScroll.setUploadedAt("2024-10-22");

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of(mockScroll));

        List<Scroll> result = scrollRepository.searchScrolls("Test Owner", 1L, "Test Title", "2024-10-22");

        assertEquals(1, result.size());

        // Check the content of the returned scroll
        Scroll returnedScroll = result.get(0);
        assertEquals(mockScroll.getId(), returnedScroll.getId(), "ID should match");
        assertEquals(mockScroll.getTitle(), returnedScroll.getTitle(), "Title should match");
        assertEquals(mockScroll.getOwner(), returnedScroll.getOwner(), "Owner should match");
        assertArrayEquals(mockScroll.getFile(), returnedScroll.getFile(), "File content should match");
        assertEquals(mockScroll.getUploadedAt(), returnedScroll.getUploadedAt(), "Uploaded At should match");

        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
    }

    // Test for searching by owner
    @Test
    public void testSearchByOwner() {
        Scroll mockScroll = new Scroll();
        mockScroll.setOwner("Test Owner");

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of(mockScroll));

        List<Scroll> result = scrollRepository.searchScrolls("Test Owner", null, null, null);

        assertEquals(1, result.size());
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM scrolls WHERE 1=1 AND owner = ?"),
                eq(new Object[] { "Test Owner" }),
                any(RowMapper.class));
    }

    // Test for no parameters (just to ensure it executes without issues)
    @Test
    public void testSearchNoParameters() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Scroll> result = scrollRepository.searchScrolls(null, null, null, null);

        assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM scrolls WHERE 1=1"),
                eq(new Object[] {}),
                any(RowMapper.class));
    }

    // Test for searching with empty owner
    @Test
    public void testSearchWithEmptyOwner() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Scroll> result = scrollRepository.searchScrolls("", null, null, null);

        assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM scrolls WHERE 1=1"),
                eq(new Object[] {}),
                any(RowMapper.class));
    }

    // Test for searching with empty title
    @Test
    public void testSearchWithEmptyTitle() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Scroll> result = scrollRepository.searchScrolls(null, null, "", null);

        assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM scrolls WHERE 1=1"),
                eq(new Object[] {}),
                any(RowMapper.class));
    }

    // Test for searching with empty uploadedAt
    @Test
    public void testSearchWithEmptyUploadedAt() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of());

        List<Scroll> result = scrollRepository.searchScrolls(null, null, null, "");

        assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM scrolls WHERE 1=1"),
                eq(new Object[] {}),
                any(RowMapper.class));
    }

    @Test
    public void testScrollRowMapperDirectly() throws Exception {
        // Create a mock ResultSet
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        // Set up the behavior of the ResultSet mock
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("title")).thenReturn("Test Title");
        when(resultSet.getBytes("file")).thenReturn("File content".getBytes());
        when(resultSet.getString("owner")).thenReturn("Test Owner");
        when(resultSet.getString("uploaded_at")).thenReturn("2024-10-22");
        when(resultSet.getInt("upload_count")).thenReturn(5);
        when(resultSet.getInt("download_count")).thenReturn(10);

        // Access the private scrollRowMapper using reflection
        Field field = ScrollRespository.class.getDeclaredField("scrollRowMapper");
        field.setAccessible(true); // Make private field accessible
        RowMapper<Scroll> rowMapper = (RowMapper<Scroll>) field.get(scrollRepository);

        // Call the RowMapper
        Scroll scroll = rowMapper.mapRow(resultSet, 0);

        // Verify the results
        assertNotNull(scroll);
        assertEquals(1L, scroll.getId());
        assertEquals("Test Title", scroll.getTitle());
        assertArrayEquals("File content".getBytes(), scroll.getFile());
        assertEquals("Test Owner", scroll.getOwner());
        assertEquals("2024-10-22", scroll.getUploadedAt());
        assertEquals(5, scroll.getUploadCount());
        assertEquals(10, scroll.getDownloadCount());
    }
}