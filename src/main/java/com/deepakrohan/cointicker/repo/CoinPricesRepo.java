package com.deepakrohan.cointicker.repo;

import com.deepakrohan.cointicker.entity.CoinPrices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;
@Repository
public interface CoinPricesRepo extends JpaRepository<CoinPrices, Long> {
    @Query("Select c from CoinPrices c WHERE c.coin.coinId = :coinId AND c.dateOfLookUp = :dateOfLookUp ")
    Optional<CoinPrices> findByCoinIdAndDateOfLookUp(@Param("coinId") Long coinId, @Param("dateOfLookUp") Date dateOfLookUp);
}
