package com.deepakrohan.cointicker.service;

import com.deepakrohan.cointicker.dto.CoinPriceDto;
import com.deepakrohan.cointicker.entity.CoinPrices;
import com.deepakrohan.cointicker.repo.CoinPricesRepo;
import com.deepakrohan.cointicker.repo.CoinRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

// TODO Store data
@Service
public class DataStoreService {

    @Autowired
    private CalcLogicService calcLogicService;

    @Autowired
    private CoinPricesRepo coinPricesRepo;

    @Autowired
    private CoinRepo coinRepo;

    public boolean peristTheData(Map<String, Double> coinInfo) {


        coinInfo.forEach((k,v) -> {
            CoinPriceDto coinPriceDto = calcLogicService.getTheDetailsToSave(k, v);
            persistIntoTable(coinPriceDto);
        });





        return true;
    }

    @Transactional
    private void persistIntoTable(CoinPriceDto coinPriceDto) {
        Optional<CoinPrices> getFromDatabaseExistingValue = coinPricesRepo.findByCoinIdAndDateOfLookUp(
                coinPriceDto.getCoin().getCoinId(), coinPriceDto.getDateOfLookUp()
        );

        if (getFromDatabaseExistingValue.isPresent()) {
            CoinPrices updateObj = getFromDatabaseExistingValue.get();
            updateObj.setMaxPriceOnDay(coinPriceDto.getMaxPriceOnDay());
            updateObj.setMinPriceOnDay(coinPriceDto.getMinPriceOnDay());
            updateObj.setLstUpdt(Instant.now());
            coinPricesRepo.save(updateObj);
        } else {
            CoinPrices coinPrices = CoinPrices.builder()
                    .coin(coinPriceDto.getCoin())
                    .createdDt(coinPriceDto.getCreatedDt())
                    .lstUpdt(Instant.now())
                    .dateOfLookUp(coinPriceDto.getDateOfLookUp())
                    .maxPriceOnDay(coinPriceDto.getMaxPriceOnDay())
                    .minPriceOnDay(coinPriceDto.getMinPriceOnDay())
                    .build();
            coinPricesRepo.save(coinPrices);

        }
    }
}
