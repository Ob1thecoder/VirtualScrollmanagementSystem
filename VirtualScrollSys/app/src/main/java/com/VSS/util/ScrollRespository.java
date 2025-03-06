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
        String sql = "INSERT INTO scrolls (title, file, owner, uploaded_at, file_type) VALUES (?, ?, ?, NOW(), ?)";
        return jdbcTemplate.update(sql, scroll.getTitle(), scroll.getFile(), scroll.getOwner(), scroll.getfileType());
    }

    // Retrieve all scrolls
    public List<Scroll> findAll() {
        String sql = "SELECT * FROM scrolls ORDER BY id ASC";
        return jdbcTemplate.query(sql, scrollRowMapper);
    }

    public int updateScroll(Scroll scroll) {
        String sql = "UPDATE scrolls SET file = ? WHERE id = ?";
        return jdbcTemplate.update(sql, scroll.getFile(), scroll.getId());
    }

    public List<Scroll> populaScrolls(){
        String sql = "SELECT * FROM scrolls ORDER BY download_count DESC LIMIT 5";
        return jdbcTemplate.query(sql, scrollRowMapper);
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
            params.add("%" + title + "%"); 
        }
        if (uploadedAt != null && !uploadedAt.isEmpty()) {
            sql.append(" AND DATE(uploaded_at) = CAST(? AS DATE)");
            params.add(uploadedAt);
        }

        
        return jdbcTemplate.query(sql.toString(), params.toArray(), scrollRowMapper);
    }
    public int addFavourite(Long userId, Long scrollId) {
        String sql = "INSERT INTO likes (user_id, scroll_id, liked_at) VALUES (?, ?, NOW())";
        return jdbcTemplate.update(sql, userId, scrollId);
    }

    public List<Scroll> getLikesByUserId(Long userId) {
        String sql = "SELECT s.* FROM scrolls s JOIN likes f ON s.id = f.scroll_id WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, scrollRowMapper);
    }
    public int removeFavourite(Long userId, Long scrollId) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND scroll_id = ?";
        return jdbcTemplate.update(sql, userId, scrollId);
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
        scroll.setFileType(rs.getString("file_type"));
        return scroll;
    };
}
