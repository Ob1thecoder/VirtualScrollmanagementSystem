package com.VSS.util;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JDBCUtilTest {

    private Connection mockConnection;

    @BeforeEach
    public void setUp() {
        mockConnection = mock(Connection.class);
    }

    // Test case for getConnection() method
    @Test
    public void testConnectionToDatabase() {
        try (Connection conn = JDBCUtil.getConnection()) {
            assertNotNull(conn, "Connection should not be null");
            assertTrue(conn.isValid(2), "Connection should be valid");
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false, "Failed to establish connection to the database: " + e.getMessage());
        }
    }

    // Test case for closeConnection() method - handling open connection
    @Test
    public void testCloseConnectionOpen() {
        try {
            // Mocking behavior for the connection: it's open and will be closed
            when(mockConnection.isClosed()).thenReturn(false);

            // Call the method under test
            JDBCUtil.closeConnection(mockConnection);

            // Verify that the close method was called
            verify(mockConnection, times(1)).close();
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false, "SQLException should not have occurred");
        }
    }

    // Test case for closeConnection() method - handling already closed connection
    @Test
    public void testCloseConnectionAlreadyClosed() {
        try {
            // Mocking behavior for the connection: it's already closed
            when(mockConnection.isClosed()).thenReturn(true);

            // Call the method under test
            JDBCUtil.closeConnection(mockConnection);

            // Verify that the close method was NOT called since the connection is already
            // closed
            verify(mockConnection, times(0)).close();
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false, "SQLException should not have occurred");
        }
    }

    @Test
    public void testCloseConnectionError() {
        try {
            // Mocking behavior for the connection: it's open and will be closed
            doThrow(new SQLException("Error closing connection")).when(mockConnection).close();

            // Call the method under test
            JDBCUtil.closeConnection(mockConnection);

            // Verify that the close method was called
            verify(mockConnection, times(1)).close();
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false, "SQLException should not have occurred");
        }
    }

}