package com.deepakrohan.cointicker.service;

import com.deepakrohan.cointicker.dto.CoinPriceDto;
import com.deepakrohan.cointicker.entity.Coin;
import com.deepakrohan.cointicker.entity.CoinPrices;
import com.deepakrohan.cointicker.repo.CoinPricesRepo;
import com.deepakrohan.cointicker.repo.CoinRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

/**
 * TODO Later this houses multiple strategies
 */
@Service
public class CalcLogicService {

    @Autowired
    private CoinRepo coinRepo;

    @Autowired
    private CoinPricesRepo coinPricesRepo;

    @Autowired
    private PushMessageService pushMessageService;

    public CoinPriceDto getTheDetailsToSave(String coinName, Double coinValue) {
        Optional<Coin> coin = coinRepo.findByCoinName(coinName);
        CoinPriceDto coinPriceDto = new CoinPriceDto();
        if(coin.isPresent()) {
            Date today = new Date(Calendar.getInstance().getTimeInMillis());
            coinPriceDto.setCoin(coin.get());

            coinPriceDto = checkForCoinPriceInRecord(coin.get(), today, coinPriceDto);

            return calculateTheMinMaxValue(coinPriceDto, coinValue);
        }

        return null;

    }

    private CoinPriceDto calculateTheMinMaxValue(CoinPriceDto coinPriceDto, Double coinValue) {
        if (coinPriceDto.getMaxPriceOnDay() != null) {
            if (coinValue > coinPriceDto.getMaxPriceOnDay()) {
                coinPriceDto.setMaxPriceOnDay(coinValue);
            } else if (coinValue < coinPriceDto.getMinPriceOnDay()) {
                coinPriceDto.setMinPriceOnDay(coinValue);
            }
        }
        if (coinPriceDto.getMinPriceOnDay() == null || coinPriceDto.getMaxPriceOnDay() == null) {
            coinPriceDto.setMinPriceOnDay(coinValue);
            coinPriceDto.setMaxPriceOnDay(coinValue);
        }
        return coinPriceDto;
    }

    private CoinPriceDto checkForCoinPriceInRecord(Coin coin, Date date, CoinPriceDto coinPriceDto) {
        Optional<CoinPrices> coinPrices = coinPricesRepo.findByCoinIdAndDateOfLookUp(coin.getCoinId(), date);

        if (coinPrices.isPresent()) {
            CoinPrices coinPrices1 = coinPrices.get();
            coinPriceDto.setMaxPriceOnDay(coinPrices1.getMaxPriceOnDay());
            coinPriceDto.setMinPriceOnDay(coinPrices1.getMinPriceOnDay());
            coinPriceDto.setDateOfLookUp(coinPrices1.getDateOfLookUp());
        } else {
            coinPriceDto.setCreatedDt(date);
            coinPriceDto.setDateOfLookUp(date);
            coinPriceDto.setLstUpdt(date);

        }

//        pushMessageService.sendMessageToQueue("Check this message");

        return coinPriceDto;
    }
}
