package com.neworg.neworg.asset;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByTickerIgnoreCase(String ticker);
    boolean existsByTickerIgnoreCase(String ticker);
}
