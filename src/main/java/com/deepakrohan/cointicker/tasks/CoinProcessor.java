package com.deepakrohan.cointicker.tasks;

import com.deepakrohan.cointicker.service.FetchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@AllArgsConstructor
public class CoinProcessor implements Tasklet {

    private FetchService fetchService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
      log.info("Start of Coin Fetch");

        System.out.println(fetchService);
      fetchService.getDataFromApi();
      return RepeatStatus.FINISHED;
    }
}
