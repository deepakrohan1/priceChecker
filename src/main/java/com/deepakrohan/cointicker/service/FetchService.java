package com.deepakrohan.cointicker.service;

import com.deepakrohan.cointicker.entity.Coin;
import com.deepakrohan.cointicker.repo.CoinRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class FetchService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DataStoreService dataStoreService;

    @Value("${server-url}")
    private String appUrl;

    @Value("${coins-list}")
    private String coinsList;

    @Value("${currency}")
    private String currency;

    @Autowired
    private CoinRepo coinRepo;


    public void getDataFromApi() throws JsonProcessingException {


        intializeTheCoinsFirstTimeIfNotPresent(coinsList);

        log.info("Inside the Service call");
        String url =  appUrl + "simple/price?ids=" + coinsList
                + "&vs_currencies=" + currency;
        log.info("the url is {}", url);
        ResponseEntity<String> response = restTemplate.getForEntity(
               url, String.class);
        log.info(String.valueOf(response.getStatusCode()));
        List<String> symbols = Arrays.asList(coinsList.split(","));

         Map<String, Double> coinsWithPrices = processTheReqObject(response.getBody(),symbols);

         dataStoreService.peristTheData(coinsWithPrices);


    }

    private Map<String, Double> processTheReqObject(String body, List<String> symbols) throws JsonProcessingException {
        Map<String, Double> coinsWithPrice = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(body);

        symbols.stream().forEach(coin -> {
            JsonNode name = root.path(coin);
            JsonNode price = name.get("usd");
            coinsWithPrice.put(coin, price.asDouble());
        });

        coinsWithPrice.forEach((k, v) -> {
            log.info("Coin {} , Price {}", k, v);
        });

        return coinsWithPrice;
    }

    @Transactional
    private void intializeTheCoinsFirstTimeIfNotPresent(String coinsList) {
        log.info("Persisting Coins here ...");
        List<String> coins = Arrays.asList(coinsList.split(","));
        coins.forEach(coin -> {
            log.info(coin);
            Optional<Coin> coin2 = coinRepo.findByCoinName(coin);
            if (!coin2.isPresent()) {

                Coin coin1 = new Coin();
                coin1.setCoinName(coin);
                coinRepo.save(coin1);
            } else {
                log.info("Coin is the {}", coin2.get());

            }
        });
    }

}
