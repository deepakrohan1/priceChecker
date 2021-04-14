package com.deepakrohan.cointicker.dto;

import com.deepakrohan.cointicker.entity.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class CoinPriceDto {

    private Coin coin;
    private Double maxPriceOnDay;
    private Double minPriceOnDay;
    private Date dateOfLookUp;
    private Date createdDt;
    private Date lstUpdt;

}
