package com.qma.conversion.repository;

import com.qma.conversion.entity.ConversionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversionHistoryRepository extends JpaRepository<ConversionHistory, Long> {

    /** All conversions by a specific user, newest first. */
    List<ConversionHistory> findByUsernameOrderByCreatedAtDesc(String username);

    /** All conversions for a given category (admin use). */
    List<ConversionHistory> findByCategoryOrderByCreatedAtDesc(String category);
}
