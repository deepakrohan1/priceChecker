package com.deepakrohan.cointicker.repo;

import com.deepakrohan.cointicker.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinRepo extends JpaRepository<Coin, Long> {
    Optional<Coin> findByCoinName(String coin);
}
