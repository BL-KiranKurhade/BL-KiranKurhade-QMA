package com.qma.uc16.repository;

import com.qma.uc16.config.DatabaseConfig;
import com.qma.uc16.model.Measurement;

import java.sql.*;
import java.util.*;

/**
 * JDBC implementation of MeasurementRepository.
 * Concepts: JDBC, Parameterized SQL, Resource Management (try-with-resources),
 * Exception Hierarchy, SQL Best Practices, Transaction Management.
 */
public class JdbcMeasurementRepository implements MeasurementRepository {

    @Override
    public Measurement save(Measurement m) {
        String sql = "INSERT INTO measurements (value, unit, category) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, m.getValue());
            ps.setString(2, m.getUnit());
            ps.setString(3, m.getCategory());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) m.setId(keys.getLong(1));
            }
            return m;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save measurement", e);
        }
    }

    @Override
    public Optional<Measurement> findById(Long id) {
        String sql = "SELECT * FROM measurements WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find measurement by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Measurement> findAll() {
        List<Measurement> list = new ArrayList<>();
        String sql = "SELECT * FROM measurements ORDER BY id";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all measurements", e);
        }
        return list;
    }

    @Override
    public List<Measurement> findByCategory(String category) {
        List<Measurement> list = new ArrayList<>();
        String sql = "SELECT * FROM measurements WHERE category = ? ORDER BY id";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find by category", e);
        }
        return list;
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM measurements WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete", e);
        }
    }

    @Override
    public int update(Measurement m) {
        String sql = "UPDATE measurements SET value=?, unit=?, category=? WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, m.getValue());
            ps.setString(2, m.getUnit());
            ps.setString(3, m.getCategory());
            ps.setLong(4, m.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update", e);
        }
    }

    private Measurement mapRow(ResultSet rs) throws SQLException {
        return new Measurement(
            rs.getLong("id"),
            rs.getDouble("value"),
            rs.getString("unit"),
            rs.getString("category")
        );
    }
}
