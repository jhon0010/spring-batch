package com.example.demo;

import com.example.demo.model.MyRecord;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public StaxEventItemReader<MyRecord> xmlItemReader() {
        StaxEventItemReader<MyRecord> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("record.xml"));

        // Define the structure of the XML file
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(MyRecord.class);

        reader.setUnmarshaller(unmarshaller);
        reader.setFragmentRootElementName("record"); // This is the root element of each record
        return reader;
    }


    @Bean
    public FlatFileItemWriter<MyRecord> csvItemWriter() {
        FlatFileItemWriter<MyRecord> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output.csv"));

        // Define the CSV format
        DelimitedLineAggregator<MyRecord> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<MyRecord> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] { "removed","uuid","id", "name", "email" });

        lineAggregator.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(lineAggregator);

        return writer;
    }


    @Bean
    public Step xmlToCsvStep() {
        return stepBuilderFactory.get("xmlToCsvStep")
                .<MyRecord, MyRecord>chunk(1)
                .reader(xmlItemReader())
                .writer(csvItemWriter())
                .build();
    }

    @Bean
    public Job xmlToCsvJob() {
        return jobBuilderFactory.get("xmlToCsvJob")
                .start(xmlToCsvStep())
                .build();
    }


}
