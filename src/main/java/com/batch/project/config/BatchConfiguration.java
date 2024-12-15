package com.batch.project.config;

import com.batch.project.dto.Attribute;
import com.batch.project.dto.InputData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Value("classpath:input.json")
    Resource resource;

    private static InputData inputData;

    private final List<String> headers = new LinkedList<>();

    Resource outputResource = null;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        inputData = mapper.readValue(resource.getFile(), InputData.class);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @StepScope
    public OutputProcessor outputProcessor(){
        return new OutputProcessor(inputData);
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("startJob")
                .flow(jobStep()).end().build();
    }

    @Bean
    @StepScope
    public ItemReader<Map<String,Object>> outputReader(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        return new CustomReader(namedParameterJdbcTemplate, inputData);
    }

    @Bean
    public Step jobStep() {
        return stepBuilderFactory.get("csv-step").<Map<String,Object>, Map<String,Object>>chunk(inputData.getBatchSize())
                .reader(outputReader(namedParameterJdbcTemplate()))
                .processor(outputProcessor())
                .writer(writer())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Map<String,Object>> writer() {
        String format = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        outputResource = new FileSystemResource(inputData.getFilePath()+ "/"+StringUtils.replace(inputData.getFileName(),"YYYYMMDDHHMMSS",format) + ".csv");
        for(Attribute attribute : inputData.getAttributes()){
            headers.add(attribute.getName());
        }
        return new FlatFileItemWriterBuilder<Map<String,Object>>()
                .name("outputCSV")
                .resource(outputResource)
                .lineAggregator(data -> data.entrySet().stream()
                        .map(entry -> entry.getValue() != null ? entry.getValue().toString():"")
                        .collect(Collectors.joining(inputData.getDelimeter())))
                .headerCallback(writer -> {
                    if(headers != null && headers.size() > 0){
                        writer.write(String.join(inputData.getDelimeter(), headers));
                    }
                })
                .build();
    }

}
