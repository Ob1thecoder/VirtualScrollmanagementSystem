package com.VSS.util;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.VSS.model.Scroll;

@Repository
public class ScrollRespository {

    private final JdbcTemplate jdbcTemplate;

    public ScrollRespository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save scroll with binary file data
    // public int saveScroll(Scroll scroll) {
    //     String sql = "INSERT INTO scrolls (title, file, owner, uploaded_at) VALUES (?, ?, ?, datetime('now'))";
    //     return jdbcTemplate.update(sql, scroll.getTitle(), scroll.getFile(), scroll.getOwner());
    // }
    public int saveScroll(Scroll scroll) {
        String sql = "INSERT INTO scrolls (title, file, owner, upload_count, download_count, uploaded_at) " +
                     "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        return jdbcTemplate.update(sql, 
            scroll.getTitle(), 
            scroll.getFile(), 
            scroll.getOwner(), 
            1,  
            0   
        );
    }

    // Retrieve all scrolls
    public List<Scroll> findAll() {
        String sql = "SELECT * FROM scrolls";
        return jdbcTemplate.query(sql, scrollRowMapper);
    }

    public int updateScroll(Scroll scroll) {
        String sql = "UPDATE scrolls SET file = ? WHERE id = ?";
        return jdbcTemplate.update(sql, scroll.getFile(), scroll.getId());
    }
    

    // Find scroll by ID
    public Scroll findScrollById(Long id) {
        String sql = "SELECT * FROM scrolls WHERE id = ?";
        List<Scroll> results = jdbcTemplate.query(sql, new Object[]{id}, scrollRowMapper);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Scroll> findScrollsByOwner(String owner) {
        String sql = "SELECT * FROM scrolls WHERE owner = ?";
        return jdbcTemplate.query(sql, new Object[]{owner}, scrollRowMapper);
    }

    public int incrementUploadCount(Long id) {
        String sql = "UPDATE scrolls SET upload_count = upload_count + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    public int incrementDownloadCount(Long id) {
        String sql = "UPDATE scrolls SET download_count = download_count + 1 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    
    
    

    public int deleteScrollById(Long id) {
        String sql = "DELETE FROM scrolls WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Scroll> searchScrolls(String owner, Long id, String title, String uploadedAt) {
        StringBuilder sql = new StringBuilder("SELECT * FROM scrolls WHERE 1=1");

        List<Object> params = new ArrayList<>();
        
        if (owner != null && !owner.isEmpty()) {
            sql.append(" AND owner = ?");
            params.add(owner);
        }
        if (id != null) {
            sql.append(" AND id = ?");
            params.add(id);
        }
        if (title != null && !title.isEmpty()) {
            sql.append(" AND title LIKE ?");
            params.add("%" + title + "%"); // partial match for the title
        }
        if (uploadedAt != null && !uploadedAt.isEmpty()) {
            sql.append(" AND DATE(uploaded_at) = CAST(? AS DATE)");
            params.add(uploadedAt);
        }

        
        return jdbcTemplate.query(sql.toString(), params.toArray(), scrollRowMapper);
    }

    // RowMapper to map ResultSet rows to Scroll objects
    private RowMapper<Scroll> scrollRowMapper = (ResultSet rs, int rowNum) -> {
        Scroll scroll = new Scroll();
        scroll.setId(rs.getLong("id"));
        scroll.setTitle(rs.getString("title"));
        scroll.setFile(rs.getBytes("file"));
        scroll.setOwner(rs.getString("owner"));
        scroll.setUploadedAt(rs.getString("uploaded_at"));
        scroll.setUploadCount(rs.getInt("upload_count"));  
        scroll.setDownloadCount(rs.getInt("download_count"));
        return scroll;
    };
}
