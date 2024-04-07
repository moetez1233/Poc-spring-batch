package com.example.PocSpringBatch.config.employeConfig;

import com.example.PocSpringBatch.models.Employe;
import com.example.PocSpringBatch.repositories.EmployeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.repository.support.Repositories;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {


    private JobBuilder jobBuilder;

    private StepBuilder stepBuilder;
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;
    @Autowired
    private EmployeRepository employeRepository;

    @Bean
    public FlatFileItemReader<Employe> readerCsv() {
        FlatFileItemReader<Employe> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/employe.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Employe> lineMapper() {
        DefaultLineMapper<Employe> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

        BeanWrapperFieldSetMapper<Employe> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Employe.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

    @Bean
    public EmployeProcessor employeProcessor() {
        return new EmployeProcessor();
    }

    @Bean
    public RepositoryItemWriter<Employe> itemWriter()

    {
        RepositoryItemWriter<Employe> itemWriter1 = new RepositoryItemWriter<Employe>();
        itemWriter1.setRepository(employeRepository);
        itemWriter1.setMethodName("save");
        return itemWriter1;
    }

    @Bean
    public Step step1(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
        return new StepBuilder("csv-step",jobRepository).<Employe, Employe>chunk(10,transactionManager)
                .reader(readerCsv())
                .processor(employeProcessor())
                .writer(itemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }
    @Bean
    public Job jobEmploye(JobRepository jobRepository,Step step1) {
        return new JobBuilder("importEmployees",jobRepository)
                .flow(step1).end().build();

    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
