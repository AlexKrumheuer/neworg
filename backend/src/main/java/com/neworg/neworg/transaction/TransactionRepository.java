package com.neworg.neworg.transaction;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByAssetId(Long assetId);
    @Query("SELECT SUM(CASE WHEN t.type = 'BUY' THEN t.quantity ELSE -t.quantity END) " +
           "FROM Transactions t WHERE t.asset.id = :assetId")
    BigDecimal getBalanceByAssetId(@Param("assetId") Long assetId);
}
