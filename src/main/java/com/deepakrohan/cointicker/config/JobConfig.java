package com.deepakrohan.cointicker.config;

import com.deepakrohan.cointicker.tasks.CoinProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step startStepOne() {
        return stepBuilderFactory.get("stepOne")
                .tasklet(new MyTaskOne())
                .build();
    }

    @Bean
    public Step startCoinDetailsFetcher() {
        return stepBuilderFactory.get("coinProcessor")
                .tasklet(new CoinProcessor())
                .build();
    }

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return  restTemplateBuilder.build();
    }

    @Bean
    public Job getJob() {
        return jobBuilderFactory.get("pricerTicker Job")
                .incrementer(new RunIdIncrementer())
                .start(startStepOne())
                .next(startCoinDetailsFetcher())
                .build();
    }

}
class MyTaskOne implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        return null;
    }
}